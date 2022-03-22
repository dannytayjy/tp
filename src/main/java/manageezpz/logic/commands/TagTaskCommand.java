package manageezpz.logic.commands;

import static java.util.Objects.requireNonNull;
import static manageezpz.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.List;

import manageezpz.logic.commands.exceptions.CommandException;
import manageezpz.model.Model;
import manageezpz.model.person.Person;
import manageezpz.model.task.Task;


public class TagTaskCommand extends Command {
    public static final String COMMAND_WORD = "tagTask";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Tag the Task to your specified Person. "
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_NAME + "EMPLOYEE_NAME"
            + "\r\n"
            + "Example: " + COMMAND_WORD + " "
            + "1 "
            + PREFIX_NAME + "Alex Yeoh";
    public static final String MESSAGE_SUCCESS = "Task has been tagged! : %1$s";
    public static final String MESSAGE_DUPLICATE_TASK = "This Task has already been assigned to the same person!";

    private int index;
    private String name;

    /**
     * Constructor for TagTaskCommand
     * @param index index of the Task to be tagged.
     * @param name Name of Employee to tag Task to.
     */
    public TagTaskCommand(int index, String name) {
        this.index = index;
        this.name = name;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Task task;
        try {
            task = model.getFilteredTaskList().get(index);
        } catch (IndexOutOfBoundsException e) {
            throw new CommandException("This Task Number is invalid. \r\n"
                    + MESSAGE_USAGE);
        }

        List<Person> personList = model.getFilteredPersonList();
        Person person = null;
        for (int i = 0; i < personList.size(); i++) {
            Person tempPerson = personList.get(i);
            if (tempPerson.getName().toString().equals(name)) {
                person = tempPerson;
                break;
            }
        }
        if (person == null) {
            throw new CommandException("Sorry, the person does not exist within our database. \r\n"
                    + MESSAGE_USAGE);
        } else if (model.isTagged(task, person)) {
            throw new CommandException(MESSAGE_DUPLICATE_TASK);
        }

        model.tagTask(task, person);
        return new CommandResult(String.format(MESSAGE_SUCCESS, task));
    }

}
