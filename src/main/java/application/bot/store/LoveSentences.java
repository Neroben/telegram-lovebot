package application.bot.store;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

@Service
public class LoveSentences {

    private static final Random random = new Random();
    public static final char NEWLINE = '\n';

    private final List<String> loveList = new ArrayList<>();

    public LoveSentences() throws IOException {
        Set<String> wasLove = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(new ClassPathResource("was.txt").getPath(), StandardCharsets.UTF_8))) {
            while (br.ready()) {
                wasLove.add(br.readLine());
            }
        }
        initLoveSentences(wasLove);
        if (loveList.isEmpty()) {
            cleanWasFile();
            initLoveSentences(Set.of());
        }
    }

    private void cleanWasFile() throws IOException {
        new FileOutputStream(new ClassPathResource("was.txt").getPath()).close();
    }

    private void initLoveSentences(Set<String> wasLove) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(new ClassPathResource("love.txt").getPath(), StandardCharsets.UTF_8))) {
            while (br.ready()) {
                String love = br.readLine();
                if (!wasLove.contains(love)) {
                    loveList.add(love);
                }
            }
        }
    }

    public String getRandom() throws IOException {
        int index = random.nextInt(loveList.size());
        String love = loveList.get(index);
        setWasLove(love);
        loveList.remove(index);
        if(loveList.isEmpty()) {
            cleanWasFile();
            initLoveSentences(Set.of());
        }
        return love;
    }

    private void setWasLove(String love) throws IOException {
        Files.write(Paths.get(new ClassPathResource("was.txt").getPath()), (love + NEWLINE).getBytes(), StandardOpenOption.APPEND);
    }

}
