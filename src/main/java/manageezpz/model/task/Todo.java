package manageezpz.model.task;

public class Todo extends Task {
    protected String type;
    protected Description description;

    /**
     * Constructor for the Todo class.
     *
     * @param taskDescription information about the task.
     */
    public Todo(Description taskDescription) {
        this.type = "todo";
        this.description = taskDescription;
    }

    /**
     * Constructor for the Todo class. This is used to initialise a new Todo
     * class for changes to be made.
     *
     * @param todo Todo task
     */
    public Todo(Todo todo) {
        this.type = todo.getType();
        this.description = todo.getDescription();
        this.isDone = todo.isDone();
        this.priority = todo.getPriority();
        this.assignees = todo.getAssignees();
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public Description getDescription() {
        return this.description;
    }

    @Override
    public String getDateTime() {
        return null;
    }

    /**
     * Returns the string representation of a todo.
     * @return a string representation of the todo, consisting of its description.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString() + getDescription();
    }
}
