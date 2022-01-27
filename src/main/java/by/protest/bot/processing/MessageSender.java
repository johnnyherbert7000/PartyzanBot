package by.protest.bot.processing;

import java.time.Instant;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import by.protest.bot.config.IBotSessionConfig;
import by.protest.bot.instance.Bot;

public class MessageSender implements Runnable, IBotSessionConfig {

	private Bot bot;

	public MessageSender(Bot bot) {
		this.bot = bot;
	}

	@Override
	public void run() {
		// FIXME initialize logger properly!
		System.out.println(Instant.now().toString() + "  Starting MessageReciever");
		while (true) {
			for (Object object = bot.sendQueue.poll(); object != null; object = bot.sendQueue.poll()) {
				send(object);
			}
			try {
				Thread.sleep(SENDER_SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void send(Object object) {
		try {
			MessageType messageType = messageType(object);
			switch (messageType) {
			case EXECUTE:
				@SuppressWarnings("unchecked")
				BotApiMethod<Message> message = (BotApiMethod<Message>) object;
				bot.execute(message);
				break;
			case IMAGE:
				SendPhoto image = (SendPhoto) object;
				bot.execute(image);
				break;
			default:
				System.out.println("Cant detect type of object. " + object);
			}
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	private MessageType messageType(Object object) {
		if (object instanceof BotApiMethod)
			return MessageType.EXECUTE;
		if (object instanceof SendPhoto)
			return MessageType.IMAGE;
		return MessageType.NOT_DETECTED;
	}

	enum MessageType {
		EXECUTE, NOT_DETECTED, IMAGE
	}
}
