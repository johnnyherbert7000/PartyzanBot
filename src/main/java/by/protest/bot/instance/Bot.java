package by.protest.bot.instance;

import java.time.Instant;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import by.protest.bot.handler.RequestHandler;
import by.protest.bot.processing.MessageReciever;
import by.protest.bot.processing.MessageSender;

@Component
public class Bot extends TelegramLongPollingBot {
	private static final Logger log = Logger.getLogger(Bot.class);

	private ExecutorService executor = null;
	private BotSession botSession = null;

	@Value("${by.rebel.bot.reconnect.pouse}")
	private long reconnectPouse;
	@Value("${by.rebel.bot.token}")
	private String botToken;
	@Value("${by.rebel.bot.name}")
	private String botName;

	@Autowired
	private RequestHandler requestHandler;

	public final Queue<Object> sendQueue = new ConcurrentLinkedQueue<>();
	public final Queue<Object> receiveQueue = new ConcurrentLinkedQueue<>();

	@Override
	public void onUpdateReceived(Update update) {
		log.debug("Receive new Update. updateID: " + update.getUpdateId());
		receiveQueue.add(update);
	}

	public void registerSession() throws TelegramApiException {
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
		try {
			botSession = telegramBotsApi.registerBot(this);
		} catch (TelegramApiRequestException e) {
			// FIXME initialize logger properly!
			System.out.println(Instant.now().toString() + "   Cant Connect. Pause " + reconnectPouse / 1000
					+ "sec and try again. Error: " + e.getMessage());
			try {
				Thread.sleep(reconnectPouse);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
				return;
			}
		}
	}

	public void closeSession() {
		botSession.stop();
	}

	public void terminateMessageProcessors() {
		executor.shutdown();
	}

	public void startMessageProcessors() {
		executor = Executors.newFixedThreadPool(2);
		executor.submit(new MessageReciever(this));
		executor.submit(new MessageSender(this));
	}

	@Override
	public String getBotUsername() {
		return botName;
	}

	@Override
	public String getBotToken() {
		return botToken;
	}

	public RequestHandler getRequestHandler() {
		return requestHandler;
	}

}
