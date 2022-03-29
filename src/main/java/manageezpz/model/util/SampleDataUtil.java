package manageezpz.model.util;

import manageezpz.model.AddressBook;
import manageezpz.model.ReadOnlyAddressBook;
import manageezpz.model.person.Email;
import manageezpz.model.person.Name;
import manageezpz.model.person.Person;
import manageezpz.model.person.Phone;
import manageezpz.model.task.Date;
import manageezpz.model.task.Deadline;
import manageezpz.model.task.Description;
import manageezpz.model.task.Event;
import manageezpz.model.task.Task;
import manageezpz.model.task.Time;
import manageezpz.model.task.Todo;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"), 0),
            new Person(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"), 0),
            new Person(new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@example.com"), 0),
            new Person(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"), 0),
            new Person(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"), 0),
            new Person(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"), 0)
        };
    }

    public static Task[] getSampleTasks() {
        return new Task[] {
            new Todo(new Description("Read Book")),
            new Deadline(new Description("Finish 160 Resins in Genshin"),
                    new Date("2022-03-15"), new Time("1800")),
            new Event(new Description("Watch Netflix"), new Date("2022-03-15"),
                    new Time("1200"), new Time("2359"))
        };
    }
    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        for (Task sampleTask : getSampleTasks()) {
            sampleAb.addTask(sampleTask);
        }
        return sampleAb;
    }
}
