package application.bot.store;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class ChatStore {

    private final Set<String> chatIdSet = new HashSet<>();

    public ChatStore() throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader(new ClassPathResource("chat.txt").getPath(), StandardCharsets.UTF_8))) {
            while (br.ready()) {
                chatIdSet.add(br.readLine());
            }
        }
    }

    public boolean save(String chatId)  {
        if (chatIdSet.add(chatId)) {
            try {
                Files.write(Paths.get(new ClassPathResource("was.txt").getPath()), (chatId + '\n').getBytes(), StandardOpenOption.APPEND);
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
