package application.bot.job;

import application.bot.store.ChatStore;
import application.bot.store.LoveSentences;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SenderMessage {

    private final ChatStore chatStore;
    private final TelegramBot telegramBot;
    private final LoveSentences loveSentences;

    public SenderMessage(ChatStore chatStore, TelegramBot telegramBot, LoveSentences loveSentences) {
        this.chatStore = chatStore;
        this.telegramBot = telegramBot;
        this.loveSentences = loveSentences;
    }

    @Scheduled(cron = "0 0 10 * * *")
    public void sendMessage() {
        chatStore.getChatIdSet().forEach(chatId -> {
            try {
                telegramBot.execute(new SendMessage(chatId, loveSentences.getRandom()));
            } catch (IOException e) {
                telegramBot.execute(new SendMessage(chatId, "У меня ошибка, я хочу плакать, " + e.getMessage()));
            }
        });
    }
}
