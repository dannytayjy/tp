package manageezpz.logic.parser;

import static manageezpz.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static manageezpz.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import manageezpz.logic.commands.AddDeadlineTaskCommand;
import manageezpz.logic.commands.AddEmployeeCommand;
import manageezpz.logic.commands.AddEventTaskCommand;
import manageezpz.logic.commands.AddTodoTaskCommand;
import manageezpz.logic.commands.ClearCommand;
import manageezpz.logic.commands.Command;
import manageezpz.logic.commands.DeleteEmployeeCommand;
import manageezpz.logic.commands.DeleteTaskCommand;
import manageezpz.logic.commands.EditCommand;
import manageezpz.logic.commands.ExitCommand;
import manageezpz.logic.commands.FindCommand;
import manageezpz.logic.commands.HelpCommand;
import manageezpz.logic.commands.ListCommand;
import manageezpz.logic.commands.MarkTaskCommand;
import manageezpz.logic.commands.TagTaskCommand;
import manageezpz.logic.commands.UnmarkTaskCommand;
import manageezpz.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class AddressBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case AddEmployeeCommand.COMMAND_WORD:
            return new AddEmployeeCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case DeleteEmployeeCommand.COMMAND_WORD:
            return new DeleteEmployeeCommandParser().parse(arguments);

        case DeleteTaskCommand.COMMAND_WORD:
            return new DeleteTaskCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
            return new FindCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommandParser().parse(arguments);

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case AddTodoTaskCommand.COMMAND_WORD:
            return new AddTodoTaskCommandParser().parse(arguments);

        case AddEventTaskCommand.COMMAND_WORD:
            return new AddEventTaskCommandParser().parse(arguments);

        case AddDeadlineTaskCommand.COMMAND_WORD:
            return new AddDeadlineTaskCommandParser().parse(arguments);

        case MarkTaskCommand.COMMAND_WORD:
            return new MarkTaskCommandParser().parse(arguments);

        case UnmarkTaskCommand.COMMAND_WORD:
            return new UnmarkTaskCommandParser().parse(arguments);

        case TagTaskCommand.COMMAND_WORD:
            return new TagTaskCommandParser().parse(arguments);

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
