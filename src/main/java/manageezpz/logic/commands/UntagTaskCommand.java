package manageezpz.logic.commands;

import static java.util.Objects.requireNonNull;
import static manageezpz.commons.core.Messages.MESSAGE_INVALID_TASK_TYPE;
import static manageezpz.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.List;

import manageezpz.commons.core.Messages;
import manageezpz.commons.core.index.Index;
import manageezpz.logic.commands.exceptions.CommandException;
import manageezpz.model.Model;
import manageezpz.model.person.Person;
import manageezpz.model.task.Deadline;
import manageezpz.model.task.Event;
import manageezpz.model.task.Task;
import manageezpz.model.task.Todo;

public class UntagTaskCommand extends Command {
    public static final String COMMAND_WORD = "untagTask";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Untags the task from the specified employee, "
            + "identified by the index number used in the displayed task list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_NAME + "EMPLOYEE_NAME\n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_NAME + "Alex Yeoh";

    public static final String MESSAGE_UNTAG_TASK_SUCCESS = "This person has been untagged from the task! : %1$s";

    public static final String MESSAGE_NO_SUCH_PERSON = "This person does not exist in the address book!";

    public static final String MESSAGE_PERSON_NOT_TAGGED_TO_TASK = "This person is not tagged to the task!";

    private final Index targetIndex;
    private final String name;

    /**
     * Initializes an UntagTaskCommand with the given targetIndex and name.
     *
     * @param targetIndex Index of the Task to tag the employee
     * @param name Name of the Employee to tag the Task to
     */
    public UntagTaskCommand(Index targetIndex, String name) {
        this.targetIndex = targetIndex;
        this.name = name;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Task> lastShownTaskList = model.getFilteredTaskList();
        List<Person> lastShownPersonList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownTaskList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        Person person = null;

        for (Person p : lastShownPersonList) {
            if (p.getName().toString().equals(name)) {
                person = p;
                break;
            }
        }

        if (person == null) {
            throw new CommandException(MESSAGE_NO_SUCH_PERSON);
        }

        Task taskToUntagPerson = lastShownTaskList.get(targetIndex.getZeroBased());
        Person personToEdit = lastShownPersonList.get(lastShownPersonList.indexOf(person));

        boolean isPersonTaggedToTask = taskToUntagPerson.getAssignees().contains(personToEdit);

        if (!isPersonTaggedToTask) {
            throw new CommandException(MESSAGE_PERSON_NOT_TAGGED_TO_TASK);
        }

        Task untaggedPersonToTask = createUntaggedPersonToTask(taskToUntagPerson, personToEdit);
        Person editedPerson = createEditedPerson(personToEdit);

        model.setTask(taskToUntagPerson, untaggedPersonToTask);
        model.setPerson(personToEdit, editedPerson);

        return new CommandResult(String.format(MESSAGE_UNTAG_TASK_SUCCESS, untaggedPersonToTask));
    }

    /**
     * Javadocs to add.
     */
    private Task createUntaggedPersonToTask(Task taskToUntagPerson, Person personToEdit) throws CommandException {
        assert taskToUntagPerson != null;
        assert personToEdit != null;

        if (taskToUntagPerson instanceof Todo) {
            Task todo = new Todo((Todo) taskToUntagPerson);
            todo.removeAssigned(personToEdit);
            return todo;
        } else if (taskToUntagPerson instanceof Deadline) {
            Task deadline = new Deadline((Deadline) taskToUntagPerson);
            deadline.removeAssigned(personToEdit);
            return deadline;
        } else if (taskToUntagPerson instanceof Event) {
            Task event = new Event((Event) taskToUntagPerson);
            event.removeAssigned(personToEdit);
            return event;
        } else {
            // The else statement should not be reached since there are
            // only three types of tasks, i.e., todo, deadline and event
            throw new CommandException(MESSAGE_INVALID_TASK_TYPE);
        }
    }

    /**
     * Javadocs to add.
     */
    private Person createEditedPerson(Person personToEdit) {
        assert personToEdit != null;

        Person editedPerson = new Person(personToEdit.getName(), personToEdit.getPhone(),
                personToEdit.getEmail(), personToEdit.getNumOfTask());
        editedPerson.decreaseTaskCount();

        return editedPerson;
    }

}
