package by.protest.bot.service;

import java.time.Instant;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import by.protest.bot.config.IBotSessionConfig;
import by.protest.bot.instance.Bot;

@Component
public class BotSessionHolder implements IBotSessionConfig{
	@Autowired
	private Bot bot;

	private boolean isSessionStarted = false;

	@PostConstruct
	private void startSession() {
		// FIXME initialize logger properly!
		System.out.println(Instant.now().toString() + "  Attempting to start session");
		try {
			bot.registerSession();
		} catch (TelegramApiException e) {
			isSessionStarted = false;
			e.printStackTrace();
			return;
		} catch (java.lang.IllegalStateException e2) {
			e2.printStackTrace();
			isSessionStarted = false;
			return;
		}
		bot.startMessageProcessors();
		isSessionStarted = true;
	}

	@Scheduled(cron = BOT_SESSION_CRON)
	private void restartSession() {
		// FIXME initialize logger properly!
		System.out.println(Instant.now().toString() + " The session is restarting");
		bot.terminateMessageProcessors();
		try {
			if (isSessionStarted) {
				bot.closeSession();
			}
		}catch(java.lang.IllegalStateException e) {
			e.printStackTrace();
			isSessionStarted = false;
		}
		startSession();
	}

}
