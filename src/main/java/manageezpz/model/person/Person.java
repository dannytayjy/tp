package manageezpz.model.person;

import static manageezpz.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {
    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;
    private int numOfTask;

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email) {
        requireAllNonNull(name, phone, email);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.numOfTask = 0;
    }

    /**
     * Constructor for the Person class. This is used to initialise a new Person
     * class for changes to be made.
     */
    public Person(Name name, Phone phone, Email email, int numOfTask) {
        requireAllNonNull(name, phone, email, numOfTask);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.numOfTask = numOfTask;
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public int getNumOfTask() {
        return numOfTask;
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    public void increaseTaskCount() {
        this.numOfTask = numOfTask + 1;
    }

    public void decreaseTaskCount() {
        this.numOfTask = numOfTask - 1;
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return otherPerson.getName().equals(getName())
                && otherPerson.getPhone().equals(getPhone())
                && otherPerson.getEmail().equals(getEmail())
                && otherPerson.getNumOfTask() == getNumOfTask();
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, numOfTask);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append("; Phone: ")
                .append(getPhone())
                .append("; Email: ")
                .append(getEmail())
                .append("; Num of Tasks: ")
                .append(getNumOfTask());
        return builder.toString();
    }
}
