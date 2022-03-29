package manageezpz.model.task;

import java.util.ArrayList;
import java.util.List;

import manageezpz.model.person.Person;

/**
 * Represents the Tasks a user could create. A <code> Task </code> object would correspond to a task
 * inputted by a user either a Todo, Deadline or Event.
 */
public abstract class Task implements Comparable<Task> {
    protected boolean isDone = false;
    protected Priority priority = Priority.NONE;

    // Data fields
    protected List<Person> assignees = new ArrayList<>();

    /**
     * Constructor for the Task class.
     * {@code Date taskDate} has a default value that will be changed if the object inheriting the Task
     * object is a Deadline or Event object. If object is a Todo object, this field will be ignored.
     */
    public Task() {
    }

    public abstract String getType();

    public abstract Description getDescription();

    public abstract String getDateTime();

    public String getStatusIcon() {
        if (this.isDone()) {
            return "X";
        } else {
            return " ";
        }
    }

    public List<Person> getAssignees() {
        return this.assignees;
    }

    public void setTaskDone() {
        this.isDone = true;
    }

    public void setTaskNotDone() {
        this.isDone = false;
    }

    public void setPriority(String priority) {
        this.priority = Priority.valueOf(priority);
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Priority getPriority() {
        return this.priority;
    }

    /**
     * Adds a Person to the Task List.
     * @param person the person to be added.
     */
    public void addAssignees(Person person) {
        this.assignees.add(person);
    }

    /**
     * Checks if the task is done or not.
     * @return true if task is done, false otherwise.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns true if both Task have the same Description.
     * This defines a weaker notion of equality between two Task.
     */
    public boolean isSameTask(Task otherTask) {
        if (otherTask == this) {
            return true;
        }

        return otherTask != null
                && otherTask.getDescription().equals(getDescription());
    }

    /**
     * Used to assign a person to this Task.
     * @param person the person to be assigned.
     */
    public void assignedTo(Person person) {
        assignees.add(person);
    }

    /**
     * Used to deallocate a person from this Task.
     * @param person the person to be deallocated.
     */
    public void removeAssigned(Person person) {
        assignees.remove(person);
    }

    /**
     * Checks whether the assignee is assigned to the task.
     * @param assignee The assignee to be searched
     * @return True if the assignee is assigned, false otherwise
     */
    public boolean haveAssignees(String assignee) {
        return assignees.stream()
                .anyMatch(person -> person.getName().fullName.equals(assignee));
    }

    @Override
    public int compareTo(Task o) {
        return Integer.compare(this.getPriority().getValue(), o.getPriority().getValue());
    }

    /**
     * Returns the string representation of the task.
     * @return a string representation of the task, consisting of its description and whether its done or not.
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] ";
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Task)) {
            return false;
        }

        Task otherTask = (Task) other;
        return otherTask.getDescription().equals(getDescription())
                && otherTask.getStatusIcon().equals(getStatusIcon());
    }

}
