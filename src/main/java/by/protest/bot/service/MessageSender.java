package by.protest.bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import by.protest.bot.instance.Bot;

@Service
public class MessageSender implements Runnable {
    private final int SENDER_SLEEP_TIME = 1000;
   @Autowired
    private Bot bot;

    @Override
    public void run() {
        System.out.println("[STARTED] MsgSender.  Bot class: " + bot);
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void send(Object object) {
        try {
            MessageType messageType = messageType(object);
            switch (messageType) {
                case EXECUTE:
                    @SuppressWarnings("unchecked") BotApiMethod<Message> message = (BotApiMethod<Message>) object;
                    bot.execute(message);
                    break;
                default:
                	System.out.println("Cant detect type of object. " + object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MessageType messageType(Object object) {
        if (object instanceof BotApiMethod) return MessageType.EXECUTE;
        return MessageType.NOT_DETECTED;
    }

    enum MessageType {
        EXECUTE, STICKER, NOT_DETECTED,
    }
}
