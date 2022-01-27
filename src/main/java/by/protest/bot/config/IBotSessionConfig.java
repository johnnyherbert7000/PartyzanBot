package by.protest.bot.config;

public interface IBotSessionConfig {
	static final int SENDER_SLEEP_TIME = 1000;
	static final int WAIT_FOR_NEW_MESSAGE_DELAY = 1000;
	//static final String BOT_SESSION_CRON = "30 * * * * ?";
	static final String BOT_SESSION_CRON = "* 0/5 * * * ?";
	//static final String BOT_SESSION_CRON = "* 30 * * * ?";
	
	static final long RECONNECT_PAUSE = 10000L;
}
