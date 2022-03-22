package manageezpz.model;

import static java.util.Objects.requireNonNull;
import static manageezpz.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static manageezpz.logic.parser.CliSyntax.PREFIX_EVENT;
import static manageezpz.logic.parser.CliSyntax.PREFIX_TODAY;
import static manageezpz.logic.parser.CliSyntax.PREFIX_TODO;

import java.util.Iterator;
import java.util.List;

import javafx.collections.ObservableList;
import manageezpz.logic.parser.Prefix;
import manageezpz.model.person.Person;
import manageezpz.model.person.UniquePersonList;
import manageezpz.model.task.Date;
import manageezpz.model.task.Deadline;
import manageezpz.model.task.Event;
import manageezpz.model.task.Task;
import manageezpz.model.task.Todo;
import manageezpz.model.task.UniqueTaskList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons;
    private final UniqueTaskList tasks;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
        tasks = new UniqueTaskList();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    /**
     * Gets the list of all tasks.
     * @return A string representing all tasks
     */
    public String listTask() {
        Iterator<Task> taskIterators = tasks.iterator();
        String result = "";
        int index = 1;

        while (taskIterators.hasNext()) {
            Task task = taskIterators.next();
            String curIndex = String.join("", String.valueOf(index), ".");
            String curTask = String.join(" ", curIndex, task.toString());
            result = String.join("\n", result, curTask);
            index++;
        }

        return result;
    }

    /**
     * Gets the list of all tasks with the options
     * @param option Option provided to filter list
     * @return All tasks that satisfy the option given
     */
    public String listTask(Prefix option) {
        if (PREFIX_TODO.equals(option)) {
            return listTodo();
        } else if (PREFIX_DEADLINE.equals(option)) {
            return listDeadline();
        } else if (PREFIX_EVENT.equals(option)) {
            return listEvent();
        } else if (PREFIX_TODAY.equals(option)) {
            return listToday();
        }
        assert true : "Invalid option, should be checked in list command parser";
        return null;
    }

    /**
     * Returns the list of all todos.
     * @return List of all todos.
     */
    private String listTodo() {
        Iterator<Task> taskIterators = tasks.iterator();
        String result = "";
        int index = 1;

        while (taskIterators.hasNext()) {
            Task task = taskIterators.next();
            if (task instanceof Todo) {
                String curIndex = String.join("", String.valueOf(index), ".");
                String curTask = String.join(" ", curIndex, task.toString());
                result = String.join("\n", result, curTask);
                index++;
            }
        }

        return result;
    }

    private String listDeadline() {
        Iterator<Task> taskIterators = tasks.iterator();
        String result = "";
        int index = 1;

        while (taskIterators.hasNext()) {
            Task task = taskIterators.next();
            if (task instanceof Deadline) {
                String curIndex = String.join("", String.valueOf(index), ".");
                String curTask = String.join(" ", curIndex, task.toString());
                result = String.join("\n", result, curTask);
                index++;
            }
        }

        return result;
    }

    private String listEvent() {
        Iterator<Task> taskIterators = tasks.iterator();
        String result = "";
        int index = 1;

        while (taskIterators.hasNext()) {
            Task task = taskIterators.next();
            if (task instanceof Event) {
                String curIndex = String.join("", String.valueOf(index), ".");
                String curTask = String.join(" ", curIndex, task.toString());
                result = String.join("\n", result, curTask);
                index++;
            }
        }

        return result;
    }

    private String listToday() {
        Iterator<Task> taskIterators = tasks.iterator();
        Date todayDate = Date.getTodayDate();
        String result = "";
        int index = 1;

        while (taskIterators.hasNext()) {
            Task task = taskIterators.next();
            Date date = getDateFromTask(task);
            if (date != null && date.equals(todayDate)) {
                String curIndex = String.join("", String.valueOf(index), ".");
                String curTask = String.join(" ", curIndex, task.toString());
                result = String.join("\n", result, curTask);
                index++;
            }
        }

        return result;
    }

    private Date getDateFromTask(Task task) {
        if (task instanceof Deadline) {
            return ((Deadline) task).getDate();
        } else if (task instanceof Event) {
            return ((Event) task).getDate();
        } else {
            return null;
        }
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }


    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);
        setPersons(newData.getPersonList());
        setTasks(newData.getTaskList());
    }

    /// task-level operations

    /**
     * Returns true if a task with the same identity as {@code task} exists in the task list.
     * @param task the task to be checked.
     * @return true if same identity otherwise false
     */
    public boolean hasTask(Task task) {
        requireNonNull(task);
        return tasks.contains(task);
    }

    public void addTask(Task t) {
        tasks.add(t);
    }

    public void setTasks(List<Task> task) {
        this.tasks.setTasks(task);
    }

    public void removeTask(Task key) {
        tasks.remove(key);
    }

    /**
     * Returns true if a todo with the same identity as {@code todo} exists in the task list.
     */
    public boolean hasTodo(Todo todo) {
        requireNonNull(todo);
        return tasks.contains(todo);
    }

    /**
     * Adds a todo to the task list.
     * The todo must not already exist in the task list.
     */
    public void addTodo(Todo todo) {
        this.tasks.add(todo);
    }

    /**
     * Returns true if a event with the same identity as {@code event} exists in the task list.
     */
    public boolean hasEvent(Event event) {
        requireNonNull(event);
        return tasks.contains(event);
    }

    /**
     * Adds an event to the task list.
     * The event must not already exist in the task list.
     */
    public void addEvent(Event event) {
        this.tasks.add(event);
    }

    /**
     * Returns true if a deadline with the same identity as {@code deadline} exists in the task list.
     */
    public boolean hasDeadline(Deadline deadline) {
        requireNonNull(deadline);
        return tasks.contains(deadline);
    }

    /**
     * Adds a deadline to the task list.
     * The deadline must not already exist in the task list.
     */
    public void addDeadline(Deadline deadline) {
        this.tasks.add(deadline);
    }

    public void markTask(Task task) {
        task.setTaskDone();
    }

    public void unmarkTask(Task task) {
        task.setTaskNotDone();
    }

    public void findTask(Task task) {
    }

    public void tagTask(Task task, Person person) {
        task.assignedTo(person);
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     */
    public void addPerson(Person p) {
        persons.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return persons.asUnmodifiableObservableList().size() + " persons";
        // TODO: refine later
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Task> getTaskList() {
        return tasks.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddressBook // instanceof handles nulls
                && persons.equals(((AddressBook) other).persons));
    }

    @Override
    public int hashCode() {
        return persons.hashCode();
    }
}
