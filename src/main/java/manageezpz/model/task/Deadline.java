package manageezpz.model.task;

import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    protected String type;
    protected Description description;
    private Date date;
    private Time time;

    /**
     * Constructor for the Deadline class.
     *
     * @param taskDescription information about the task.
     */
    public Deadline(Description taskDescription, Date date, Time time) {
        this.type = "deadline";
        this.description = taskDescription;
        this.date = date;
        this.time = time;
    }

    /**
     * Constructor for the Deadline class. This is used to initialise a new Deadline
     * class for changes to be made.
     *
     * @param deadline Deadline task
     */
    public Deadline(Deadline deadline) {
        this.type = deadline.getType();
        this.description = deadline.getDescription();
        this.date = deadline.getDate();
        this.time = deadline.getTime();
        this.isDone = deadline.isDone();
        this.priority = deadline.getPriority();
        this.assignees = deadline.getAssignees();
    }

    public Date getDate() {
        return this.date;
    }

    public Time getTime() {
        return this.time;
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
        return "by " + date.format(DateTimeFormatter.ofPattern("MMM dd yyyy"))
                + " " + time.format(DateTimeFormatter.ofPattern("h:mm a"));
    }

    /**
     * Returns the string representation of a deadline.
     * @return a string representation of the deadline, consisting of its description
     * and formatted date and time.
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + getDescription()
                + " (by: " + date.format(DateTimeFormatter.ofPattern("MMM dd yyyy"))
                + " " + time.format(DateTimeFormatter.ofPattern("h:mm a")) + ")";
    }
}
