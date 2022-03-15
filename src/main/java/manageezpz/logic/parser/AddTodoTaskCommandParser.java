package manageezpz.logic.parser;

import static manageezpz.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static manageezpz.logic.parser.CliSyntax.PREFIX_DESCRIPTION;

import java.util.stream.Stream;

import manageezpz.logic.commands.AddTodoTaskCommand;
import manageezpz.logic.parser.exceptions.ParseException;
import manageezpz.model.task.Description;
import manageezpz.model.task.Todo;

public class AddTodoTaskCommandParser implements Parser<AddTodoTaskCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the AddEmployeeCommand
     * and returns an AddEmployeeCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddTodoTaskCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimapTodo =
                ArgumentTokenizer.tokenize(args, PREFIX_DESCRIPTION);

        if (!arePrefixesPresent(argMultimapTodo, PREFIX_DESCRIPTION)
                || !argMultimapTodo.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddTodoTaskCommand.MESSAGE_USAGE));
        }
        Description description = ParserUtil.parseDescription(argMultimapTodo.getValue(PREFIX_DESCRIPTION).get());

        Todo todoTask = new Todo(description);

        return new AddTodoTaskCommand(todoTask);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}