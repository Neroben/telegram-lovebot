package application.bot;

import application.bot.store.ChatStore;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;

@Service
public class Telegram {

    public Telegram(ChatStore chatStore, TelegramBot telegramBot) {
        telegramBot.setUpdatesListener(updates -> {
            if (chatStore.save(Long.toString(updates.get(0).message().chat().id()))) {
                telegramBot.execute(new SendMessage(updates.get(0).message().chat().id(), "Я тебя запомнил. Это бот любви! Жди 9:30"));
            } else {
                telegramBot.execute(new SendMessage(updates.get(0).message().chat().id(), "Я все ещё помню тебя. Люблю тебя. Жди 9:30"));
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

}