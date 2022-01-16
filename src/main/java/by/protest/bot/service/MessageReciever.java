package by.protest.bot.service;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import by.protest.bot.command.Command;
import by.protest.bot.command.ParsedCommand;
import by.protest.bot.command.Parser;
import by.protest.bot.handler.RequestHandler;
import by.protest.bot.instance.Bot;

@Service
public class MessageReciever implements Runnable {
    private static final Logger log = Logger.getLogger(MessageReciever.class);
    private final int WAIT_FOR_NEW_MESSAGE_DELAY = 1000;
    @Autowired
    private Bot bot;
    @Autowired
    private RequestHandler requestHandler;
    
    private Parser parser;

	@PostConstruct
	private void initializeParser() {
		this.parser = new Parser(bot.getBotUsername());
	}
    
    
    @Override
    public void run() {
        log.info("[STARTED] MsgReciever.  Bot class: " + bot);
        while (true) {
            for (Object object = bot.receiveQueue.poll(); object != null; object = bot.receiveQueue.poll()) {
                log.debug("New object for analyze in queue " + object.toString());
                analyze(object);
            }
            try {
                Thread.sleep(WAIT_FOR_NEW_MESSAGE_DELAY);
            } catch (InterruptedException e) {
                log.error("Catch interrupt. Exit", e);
                return;
            }
        }
    }

    private void analyze(Object object) {
        if (object instanceof Update) {
            Update update = (Update) object;
            log.debug("Update recieved: " + update.toString());
			if (update.getMessage() != null)
				analyzeForUpdateType(update);
        } else log.warn("Cant operate type of object: " + object.toString());
    }

    private void analyzeForUpdateType(Update update) {
        Message message = update.getMessage();
        String chatId = Long.toString(message.getChatId());

        ParsedCommand parsedCommand = new ParsedCommand(Command.NONE, "");

        if (message.hasText()) {
            parsedCommand = parser.getParsedCommand(message.getText());
        } 
        requestHandler.operate(chatId, parsedCommand, update);
    }

}
