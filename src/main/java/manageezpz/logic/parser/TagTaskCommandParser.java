package manageezpz.logic.parser;

import static manageezpz.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static manageezpz.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.stream.Stream;

import manageezpz.logic.commands.TagTaskCommand;
import manageezpz.logic.parser.exceptions.ParseException;


public class TagTaskCommandParser implements Parser<TagTaskCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddDeadlineTaskCommand
     * and returns an AddDeadlineTaskCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public TagTaskCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimapDeadline =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME);

        if (!arePrefixesPresent(argMultimapDeadline, PREFIX_NAME)
                || !argMultimapDeadline.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    TagTaskCommand.MESSAGE_USAGE));
        }
        String name = argMultimapDeadline.getValue(PREFIX_NAME).get();
        String[] argsArr = args.trim().split(" ");
        int index = ParserUtil.parseIndex(argsArr[0]).getZeroBased();
        return new TagTaskCommand(index, name);
    }


    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
