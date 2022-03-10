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
import java.util.HashSet;
import java.util.Set;

@Service
public class ChatStore {

    public final String chatFile = Paths.get("src/main/resources/chat.txt").toAbsolutePath().toString();
    private final Set<String> chatIdSet = new HashSet<>();

    public ChatStore() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(chatFile, StandardCharsets.UTF_8))) {
            while (br.ready()) {
                chatIdSet.add(br.readLine());
            }
        }
    }

    public boolean save(String chatId) {
        if (chatIdSet.add(chatId)) {
            try {
                Files.write(Paths.get("src/main/resources/was.txt").toAbsolutePath(), (chatId + '\n').getBytes(), StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    public Set<String> getChatIdSet() {
        return Collections.unmodifiableSet(chatIdSet);
    }
}
