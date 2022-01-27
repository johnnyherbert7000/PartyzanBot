package by.protest.bot.processing;

import java.time.Instant;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import by.protest.bot.command.Command;
import by.protest.bot.command.ParsedCommand;
import by.protest.bot.command.Parser;
import by.protest.bot.config.IBotSessionConfig;
import by.protest.bot.instance.Bot;

public class MessageReciever implements Runnable, IBotSessionConfig {
	   
	public MessageReciever(Bot bot) {
		this.bot = bot;
		this.parser = new Parser(bot.getBotUsername());
	}

    private Bot bot;
    private Parser parser;

	@Override
	public void run() {
		// FIXME initialize logger properly!
		System.out.println(Instant.now().toString() + "  Starting MessageReciever");
		while (true) {
			for (Object object = bot.receiveQueue.poll(); object != null; object = bot.receiveQueue.poll()) {
				analyze(object);
			}
			try {
				Thread.sleep(WAIT_FOR_NEW_MESSAGE_DELAY);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void analyze(Object object) {
		if (object instanceof Update) {
			Update update = (Update) object;
			if (update.getMessage() != null)
				analyzeForUpdateType(update);
		}
	}

    private void analyzeForUpdateType(Update update) {
        Message message = update.getMessage();
        String chatId = Long.toString(message.getChatId());
        ParsedCommand parsedCommand = new ParsedCommand(Command.NONE, "");
        if (message.hasText()) {
            parsedCommand = parser.getParsedCommand(message.getText());
        } 
        bot.getRequestHandler().operate(chatId, parsedCommand, update);
    }

}
