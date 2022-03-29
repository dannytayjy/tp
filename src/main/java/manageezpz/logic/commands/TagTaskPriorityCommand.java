package manageezpz.logic.commands;

import static java.util.Objects.requireNonNull;
import static manageezpz.commons.core.Messages.MESSAGE_INVALID_TASK_TYPE;
import static manageezpz.logic.parser.CliSyntax.PREFIX_PRIORITY;

import java.util.List;

import manageezpz.commons.core.Messages;
import manageezpz.commons.core.index.Index;
import manageezpz.logic.commands.exceptions.CommandException;
import manageezpz.model.Model;
import manageezpz.model.task.Deadline;
import manageezpz.model.task.Event;
import manageezpz.model.task.Priority;
import manageezpz.model.task.Task;
import manageezpz.model.task.Todo;

/**
 * Tags a priority to the task identified using its displayed index from the address book.
 */
public class TagTaskPriorityCommand extends Command {
    public static final String COMMAND_WORD = "tagPriority";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Tags the specified priority to the task, "
            + "identified by the index number used in the displayed task list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_PRIORITY + "PRIORITY_VALUE " + "(must be either NONE/LOW/MEDIUM/HIGH)\n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_PRIORITY + "HIGH";

    public static final String MESSAGE_TAG_PRIORITY_SUCCESS =
            "Task has been tagged with the appropriate priority! %1$s";

    private final Index targetIndex;
    private final Priority priority;

    /**
     * Initializes a TagTaskPriorityCommand with the given targetIndex and priority.
     *
     * @param targetIndex Index of the Task to tag the priority level
     * @param priority Priority level of the Task
     */
    public TagTaskPriorityCommand(Index targetIndex, Priority priority) {
        this.targetIndex = targetIndex;
        this.priority = priority;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Task> lastShownList = model.getFilteredTaskList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        Task taskToTagPriority = lastShownList.get(targetIndex.getZeroBased());
        Task taggedPriorityTask = createTaggedPriorityTask(taskToTagPriority, priority);

        model.setTask(taskToTagPriority, taggedPriorityTask);
        return new CommandResult(String.format(MESSAGE_TAG_PRIORITY_SUCCESS, taggedPriorityTask));
    }

    /**
     * Creates and returns a {@code Task} with the details of {@code taskToTagPriority}
     * and updated tagged priority.
     */
    private Task createTaggedPriorityTask(Task taskToTagPriority, Priority priority) throws CommandException {
        assert taskToTagPriority != null;
        assert priority != null;

        if (taskToTagPriority instanceof Todo) {
            Task todo = new Todo((Todo) taskToTagPriority);
            todo.setPriority(priority);
            return todo;
        } else if (taskToTagPriority instanceof Deadline) {
            Task deadline = new Deadline((Deadline) taskToTagPriority);
            deadline.setPriority(priority);
            return deadline;
        } else if (taskToTagPriority instanceof Event) {
            Task event = new Event((Event) taskToTagPriority);
            event.setPriority(priority);
            return event;
        } else {
            // The else statement should not be reached since there are
            // only three types of tasks, i.e., todo, deadline and event
            throw new CommandException(MESSAGE_INVALID_TASK_TYPE);
        }
    }
}
