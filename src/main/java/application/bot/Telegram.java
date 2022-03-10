package application.bot;

import application.bot.store.ChatStore;
import application.bot.store.LoveSentences;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class Telegram {

    private final ChatStore chatStore;
    private final TelegramBot telegramBot;
    private final LoveSentences loveSentences;

    public Telegram(ChatStore chatStore, TelegramBot telegramBot, LoveSentences loveSentences) {
        this.chatStore = chatStore;
        this.telegramBot = telegramBot;
        this.loveSentences = loveSentences;
        telegramBot.setUpdatesListener(updates -> {
            updates.forEach(this::proceedMessage);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void proceedMessage(Update update) {
        String text = update.message().text();
        if (text.startsWith("<-ADMIN->")) {
            String[] command = text.split(" ");
            switch (command[1]) {
                case "INFO" -> proceedInfo(update);
                case "+" -> loveSentences.store(text.substring(12));
                case "GET_ALL" -> telegramBot.execute(new SendMessage(update.message().chat().id(),
                        loveSentences.toString()));
                default -> saveChat(update.message().chat().id());
            }

        } else {
            saveChat(update.message().chat().id());
        }

    }

    private void proceedInfo(Update update) {
        telegramBot.execute(new SendMessage(update.message().chat().id(), "Осталось сообщений : "
                + loveSentences.getSizeLoveList()));
    }

    private void saveChat(Long chatId) {
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