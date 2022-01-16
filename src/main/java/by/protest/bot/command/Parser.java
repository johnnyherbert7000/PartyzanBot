package by.protest.bot.command;

import java.util.List;

import org.apache.log4j.Logger;

import com.vdurmont.emoji.EmojiParser;


public class Parser {
    private static final Logger log = Logger.getLogger(Parser.class);
    private final String PREFIX_FOR_COMMAND = "/";
    private final String DELIMITER_COMMAND_BOTNAME = "@";
    private String botName;
    


    public Parser(String botName) {
        this.botName = botName;
    }

    public String getBotName() {
		return botName;
	}

	public void setBotName(String botName) {
		this.botName = botName;
	}

	public ParsedCommand getParsedCommand(String text) {
        String trimText = "";
        if (text != null) trimText = text.trim();
        ParsedCommand result = new ParsedCommand(Command.NONE, trimText);

        if ("".equals(trimText)) return result;
        Pair commandAndText = getDelimitedCommandFromText(trimText);
        if (isCommand(commandAndText.getKey())) {
            if (isCommandForMe(commandAndText.getKey())) {
                String commandForParse = cutCommandFromFullText(commandAndText.getKey());
                Command commandFromText = getCommandFromText(commandForParse);
                result.setText(commandAndText.getValue());
                result.setCommand(commandFromText);
            } else {
                result.setCommand(Command.NOTFORME);
                result.setText(commandAndText.getValue());
            }

        }
        if (result.getCommand() == Command.NONE) {
            List<String> emojiContainsInText = EmojiParser.extractEmojis(result.getText());
            if (emojiContainsInText.size() > 0) result.setCommand(Command.TEXT_CONTAIN_EMOJI);
        }
        return result;
    }

    private String cutCommandFromFullText(String text) {
        return text.contains(DELIMITER_COMMAND_BOTNAME) ?
                text.substring(1, text.indexOf(DELIMITER_COMMAND_BOTNAME)) :
                text.substring(1);
    }

    private Command getCommandFromText(String text) {
        String upperCaseText = text.toUpperCase().trim();
        Command command = Command.NONE;
        try {
            command = Command.valueOf(upperCaseText);
        } catch (IllegalArgumentException e) {
            log.debug("Can't parse command: " + text);
        }
        return command;
    }

    private Pair getDelimitedCommandFromText(String trimText) {
        Pair commandText;

        if (trimText.contains(" ")) {
            int indexOfSpace = trimText.indexOf(" ");
            commandText = new Pair(trimText.substring(0, indexOfSpace), trimText.substring(indexOfSpace + 1));
        } else commandText = new Pair(trimText, "");
        return commandText;
    }

    private boolean isCommandForMe(String command) {
        if (command.contains(DELIMITER_COMMAND_BOTNAME)) {
            String botNameForEqual = command.substring(command.indexOf(DELIMITER_COMMAND_BOTNAME) + 1);
            return botName.equals(botNameForEqual);
        }
        return true;
    }

    private boolean isCommand(String text) {
        return text.startsWith(PREFIX_FOR_COMMAND);
    }
    
    private class Pair{
    	private String key;
    	private String value;
		public Pair(String key, String value) {
			super();
			this.key = key;
			this.value = value;
		}
		protected String getKey() {
			return key;
		}
		protected String getValue() {
			return value;
		}
    	
    }
}
