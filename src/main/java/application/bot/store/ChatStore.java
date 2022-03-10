package application.bot.store;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChatStore {

    public final String chatFile = Paths.get("src/main/resources/chat.txt").toAbsolutePath().toString();
    private final Set<String> chatIdSet;

    public ChatStore() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(chatFile, StandardCharsets.UTF_8))) {
            chatIdSet = br.lines().collect(Collectors.toSet());
        }
    }

    public boolean save(String chatId) throws IOException {
        if (chatIdSet.add(chatId)) {
            Files.writeString(Paths.get("src/main/resources/was.txt").toAbsolutePath(),
                    chatId + System.lineSeparator(), StandardOpenOption.APPEND);
            return true;
        }
        return false;
    }

    public Set<String> getChatIdSet() {
        return Collections.unmodifiableSet(chatIdSet);
    }
}
