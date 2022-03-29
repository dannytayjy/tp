package manageezpz.logic.parser;

import static manageezpz.commons.core.Messages.MESSAGE_EMPTY_PRIORITY;
import static manageezpz.commons.core.Messages.MESSAGE_EMPTY_TASK_NUMBER;
import static manageezpz.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static manageezpz.commons.core.Messages.MESSAGE_INVALID_PRIORITY;
import static manageezpz.logic.parser.CliSyntax.PREFIX_PRIORITY;

import java.util.stream.Stream;

import manageezpz.commons.core.index.Index;
import manageezpz.logic.commands.TagTaskPriorityCommand;
import manageezpz.logic.parser.exceptions.ParseException;
import manageezpz.model.task.Priority;

public class TagTaskPriorityCommandParser implements Parser<TagTaskPriorityCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the TagTaskPriorityCommand
     * and returns a TagTaskPriorityCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public TagTaskPriorityCommand parse(String args) throws ParseException {
        Index index;
        Priority priority;

        ArgumentMultimap argMultimapPriority =
                ArgumentTokenizer.tokenize(args, PREFIX_PRIORITY);

        if (!arePrefixesPresent(argMultimapPriority, PREFIX_PRIORITY)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    TagTaskPriorityCommand.MESSAGE_USAGE));
        }

        if (argMultimapPriority.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_EMPTY_TASK_NUMBER,
                    TagTaskPriorityCommand.MESSAGE_USAGE));
        }

        try {
            String[] argsArr = args.trim().split(" ");
            index = ParserUtil.parseIndex(argsArr[0]);
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    TagTaskPriorityCommand.MESSAGE_USAGE), pe);
        }

        String priorityString = argMultimapPriority.getValue(PREFIX_PRIORITY).get();
        if (priorityString.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_EMPTY_PRIORITY,
                    TagTaskPriorityCommand.MESSAGE_USAGE));
        }

        try {
            priority = Priority.valueOf(priorityString.toUpperCase());
        } catch (IllegalArgumentException ie) {
            throw new ParseException(String.format(MESSAGE_INVALID_PRIORITY,
                    TagTaskPriorityCommand.MESSAGE_USAGE), ie);
        }

        return new TagTaskPriorityCommand(index, priority);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
