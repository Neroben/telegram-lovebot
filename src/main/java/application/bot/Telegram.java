package application.bot;

import application.bot.store.ChatStore;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class Telegram {

    public Telegram(ChatStore chatStore, TelegramBot telegramBot) {
        telegramBot.setUpdatesListener(updates -> {
            saveChat(chatStore, telegramBot, updates);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void saveChat(ChatStore chatStore, TelegramBot telegramBot, List<Update> updates) {
        Long chatId = updates.get(0).message().chat().id();
        try {
            if (chatStore.save(Long.toString(chatId))) {
                telegramBot.execute(new SendMessage(chatId, "Я тебя запомнил. Это бот любви! Жди 9:30"));
            } else {
                telegramBot.execute(new SendMessage(chatId, "Я все ещё помню тебя. Люблю тебя. Жди 9:30"));
            }
        } catch (IOException e) {
            telegramBot.execute(new SendMessage(chatId,
                    "У меня ошибка, скажи Саше, чтобы я не плакал " + e.getMessage() + " " + chatId));
        }
    }

}