package manageezpz.storage;

import java.util.List;
import java.util.StringJoiner;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javafx.collections.ObservableList;
import manageezpz.commons.exceptions.IllegalValueException;
import manageezpz.model.person.Person;
import manageezpz.model.task.Date;
import manageezpz.model.task.Deadline;
import manageezpz.model.task.Description;
import manageezpz.model.task.Event;
import manageezpz.model.task.Task;
import manageezpz.model.task.Time;
import manageezpz.model.task.Todo;
import manageezpz.model.tasktag.Tag;

/**
 * Jackson-friendly version of {@link Task}.
 */
class JsonAdaptedTask {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Task's %s field is missing!";

    private final String description;
    private final String type;
    private final String date;
    private String deadlineTime;
    private String eventStartTime;
    private String eventEndTime;
    private String status;
    private String tag;


    /**
     * Constructs a {@code JsonAdaptedTask} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedTask(@JsonProperty("type") String type, @JsonProperty("status") String status,
                           @JsonProperty("description") String description,
                           @JsonProperty("date") String date, @JsonProperty("deadlineTime") String deadlineTime,
                           @JsonProperty("eventStartTime") String eventStartTime,
                           @JsonProperty("eventEndTime") String eventEndTime,
                           @JsonProperty("tag") String tag) {
        this.description = new Description(description).toString();
        this.status = status;
        this.type = type;
        this.date = date;
        this.deadlineTime = deadlineTime;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
        this.tag = tag;
    }

    /**
     * Converts a given {@code Task} into this class for Jackson use.
     */

    public JsonAdaptedTask(Task source) {
        description = source.getDescription().toString(); // Generally for all tasks
        type = source.getType(); // Generally for all tasks
        status = source.getStatusIcon(); // Generally for all tasks
        if (source instanceof Deadline) {
            this.date = ((Deadline) source).getDate().getDate(); // For Deadline
            this.deadlineTime = ((Deadline) source).getTime().getTime(); // For Deadline
        } else if ((source instanceof Event)) {
            this.date = ((Event) source).getDate().getDate(); // For Event
            this.eventStartTime = ((Event) source).getStartTime().getTime(); // For Event
            this.eventEndTime = ((Event) source).getEndTime().getTime(); // For Event
        } else {
            this.date = "";
            this.deadlineTime = "";
            this.eventStartTime = "";
            this.eventEndTime = "";
        }
        List<Person> personList = source.getAssignees();
        StringJoiner joiner = new StringJoiner(", ");
        personList.forEach(item -> joiner.add(item.getName().toString()));
        this.tag = joiner.toString();
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Task} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Task toModelType(ObservableList<Person> persons) throws IllegalValueException {
        if (description == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Description.class.getSimpleName()));
        }
        if (type == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Task.class.getSimpleName()));
        }
        if (status == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Task.class.getSimpleName()));
        }
        if (tag == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Tag.class.getSimpleName()));
        }

        Description desc = new Description(description);
        boolean isDone = status.equals("X");

        if (type.equals("todo")) {
            Todo newTodo = new Todo(desc);
            if (isDone) {
                newTodo.setTaskDone();
            }

            String[] tagList = tag.split(",");
            for (int i = 0; i < tagList.length; i++) {
                String currentTag = tagList[i].trim();
                for (int j = 0; j < persons.size(); j++) {
                    Person matchedPerson = null;

                    if (persons.get(j).getName().toString().equals(currentTag)) {
                        matchedPerson = persons.get(j);
                        newTodo.addAssignees(matchedPerson);
                    }
                }
            }
            return newTodo;
        } else if (type.equals("deadline")) {
            Date currDeadlineDate = new Date(date);
            Time currDeadlineTime = new Time(deadlineTime);
            Deadline newDeadline = new Deadline(desc, currDeadlineDate, currDeadlineTime);
            if (isDone) {
                newDeadline.setTaskDone();
            }
            String[] tagList = tag.split(",");
            for (int i = 0; i < tagList.length; i++) {
                String currentTag = tagList[i].trim();
                for (int j = 0; j < persons.size(); j++) {
                    Person matchedPerson = null;
                    if (persons.get(j).getName().toString().equals(currentTag)) {
                        matchedPerson = persons.get(j);
                        newDeadline.addAssignees(matchedPerson);
                    }
                }
            }
            return newDeadline;
        } else {
            Date currEventDate = new Date(date);
            Time currEventStartTime = new Time(eventStartTime);
            Time currEventEndTime = new Time(eventEndTime);
            Event newEvent = new Event(desc, currEventDate, currEventStartTime, currEventEndTime);
            if (isDone) {
                newEvent.setTaskDone();
            }
            String[] tagList = tag.split(",");
            for (int i = 0; i < tagList.length; i++) {
                String currentTag = tagList[i].trim();
                for (int j = 0; j < persons.size(); j++) {
                    Person matchedPerson = null;
                    if (persons.get(j).getName().toString().equals(currentTag)) {
                        matchedPerson = persons.get(j);
                        newEvent.addAssignees(matchedPerson);
                    }
                }
            }
            return newEvent;
        }
    }
}
