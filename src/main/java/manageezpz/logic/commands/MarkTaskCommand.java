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
 * Marks a task as done, that is identified using its displayed index from the address book.
 */
public class MarkTaskCommand extends Command {
    public static final String COMMAND_WORD = "markTask";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the task as done, identified by the index number used in the displayed task list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_MARK_TASK_SUCCESS = "Task marked as done: %1$s";

    private final Index targetIndex;

    /**
     * Initializes a MarkTaskCommand with the given targetIndex.
     *
     * @param targetIndex Index of the Task to be marked as done
     */
    public MarkTaskCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Task> lastShownList = model.getFilteredTaskList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        Task taskToMark = lastShownList.get(targetIndex.getZeroBased());
        Task markedTask = createMarkedTask(taskToMark);

        model.setTask(taskToMark, markedTask);
        return new CommandResult(String.format(MESSAGE_MARK_TASK_SUCCESS, markedTask));
    }

    /**
     * Creates and returns a {@code Task} with the details of {@code taskToMark}
     * and marked as done.
     */
    private Task createMarkedTask(Task taskToMark) throws CommandException {
        assert taskToMark != null;

        if (taskToMark instanceof Todo) {
            Task todo = new Todo((Todo) taskToMark);
            todo.setTaskDone();
            return todo;
        } else if (taskToMark instanceof Deadline) {
            Task deadline = new Deadline((Deadline) taskToMark);
            deadline.setTaskDone();
            return deadline;
        } else if (taskToMark instanceof Event) {
            Task event = new Event((Event) taskToMark);
            event.setTaskDone();
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
                || (other instanceof MarkTaskCommand // instanceof handles nulls
                && targetIndex.equals(((MarkTaskCommand) other).targetIndex)); // state check
    }
}
