package by.protest.bot.instance;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import by.protest.bot.command.Parser;
import by.protest.bot.service.MessageReciever;
import by.protest.bot.service.MessageSender;

@Component
public class Bot extends TelegramLongPollingBot {
    private static final Logger log = Logger.getLogger(Bot.class);
    private final int RECONNECT_PAUSE = 10000;
    
    @Value("${by.rebel.bot.token}")
    private String botToken;
    @Value("${by.rebel.bot.name}")
    private String botName;
    
    @Autowired
    private MessageReciever messageReciever;
    
    @Autowired
    private MessageSender messageSender;

	public final Queue<Object> sendQueue = new ConcurrentLinkedQueue<>();
    public final Queue<Object> receiveQueue = new ConcurrentLinkedQueue<>();
    

    @Override
    public void onUpdateReceived(Update update) {
        log.debug("Receive new Update. updateID: " + update.getUpdateId());
        receiveQueue.add(update);
    }


    @PostConstruct
    private void initializeConnection() throws TelegramApiException {
    	
    	TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(this);
            log.info("[STARTED] TelegramAPI. Bot Connected. Bot class: " + this);
        } catch (TelegramApiRequestException e) {
            log.error("Cant Connect. Pause " + RECONNECT_PAUSE / 1000 + "sec and try again. Error: " + e.getMessage());
            try {
                Thread.sleep(RECONNECT_PAUSE);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
                return;
            }
        }
		Thread receiver = new Thread(messageReciever);
		receiver.setDaemon(true);
		receiver.setName("MsgReciever");
		receiver.setPriority(2);
		receiver.start();
		Thread sender = new Thread(messageSender);
		sender.setDaemon(true);
		sender.setName("MsgSender");
		sender.setPriority(1);
		sender.start();
    }


	@Override
	public String getBotUsername() {
		return botName;
	}


	@Override
	public String getBotToken() {
		return botToken;
	}

}
