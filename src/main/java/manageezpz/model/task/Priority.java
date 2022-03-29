package manageezpz.model.task;

public enum Priority {
    HIGH(0),
    MEDIUM(1),
    LOW(2),
    NONE(3);

    private final int value;

    /**
     * Constructor to initialize an instance of Priority enumeration
     * with value field.
     *
     * @param value Value of the Priority
     */
    Priority(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
