package manageezpz.logic.commands;

import static java.util.Objects.requireNonNull;
import static manageezpz.commons.core.Messages.MESSAGE_INVALID_TASK_TYPE;

import java.util.List;

import manageezpz.commons.core.Messages;
import manageezpz.commons.core.index.Index;
import manageezpz.logic.commands.exceptions.CommandException;
import manageezpz.model.Model;
import manageezpz.model.task.Deadline;
import manageezpz.model.task.Event;
import manageezpz.model.task.Task;
import manageezpz.model.task.Todo;

/**
 * Marks a task as not done yet, that is identified using its displayed index from the address book.
 */
public class UnmarkTaskCommand extends Command {
    public static final String COMMAND_WORD = "unmarkTask";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the task as not done yet, identified by the index number used in the displayed task list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_UNMARK_TASK_SUCCESS = "Task marked as not done yet: %1$s";

    private final Index targetIndex;

    /**
     * Initializes a UnmarkTaskCommand with the given targetIndex.
     *
     * @param targetIndex Index of the Task to be marked as not done yet
     */
    public UnmarkTaskCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Task> lastShownList = model.getFilteredTaskList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        Task taskToUnmark = lastShownList.get(targetIndex.getZeroBased());
        Task unmarkedTask = createUnmarkedTask(taskToUnmark);

        model.setTask(taskToUnmark, unmarkedTask);
        return new CommandResult(String.format(MESSAGE_UNMARK_TASK_SUCCESS, unmarkedTask));
    }

    /**
     * Creates and returns a {@code Task} with the details of {@code taskToUnmark}
     * and marked as not done yet.
     */
    private Task createUnmarkedTask(Task taskToUnmark) throws CommandException {
        assert taskToUnmark != null;

        if (taskToUnmark instanceof Todo) {
            Task todo = new Todo((Todo) taskToUnmark);
            todo.setTaskNotDone();
            return todo;
        } else if (taskToUnmark instanceof Deadline) {
            Task deadline = new Deadline((Deadline) taskToUnmark);
            deadline.setTaskNotDone();
            return deadline;
        } else if (taskToUnmark instanceof Event) {
            Task event = new Event((Event) taskToUnmark);
            event.setTaskNotDone();
            return event;
        } else {
            // The else statement should not be reached since there are
            // only three types of tasks, i.e., todo, deadline and event
            throw new CommandException(MESSAGE_INVALID_TASK_TYPE);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UnmarkTaskCommand // instanceof handles nulls
                && targetIndex.equals(((UnmarkTaskCommand) other).targetIndex)); // state check
    }
}
