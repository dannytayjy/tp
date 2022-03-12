package manageezpz.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_PHONE = new Prefix("p/");
    public static final Prefix PREFIX_EMAIL = new Prefix("e/");
    public static final Prefix PREFIX_DESCRIPTION = new Prefix("desc/");
    public static final Prefix PREFIX_DATETIME = new Prefix("by/");
    public static final Prefix PREFIX_TIME = new Prefix("at/");
    public static final Prefix PREFIX_TODO = new Prefix("/todo");
    public static final Prefix PREFIX_EVENT = new Prefix("/event");
    public static final Prefix PREFIX_DEADLINE = new Prefix("/deadline");
}
