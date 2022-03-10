package application.bot.store;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LoveSentences {

    private static final Random random = new Random();
    public static final String LINE_SEPARATOR = System.lineSeparator();
    public final String loveFile = Paths.get("src/main/resources/love.txt").toAbsolutePath().toString();
    public final String wasFile = Paths.get("src/main/resources/was.txt").toAbsolutePath().toString();

    private List<String> loveList;

    public LoveSentences() throws IOException {
        Set<String> wasLove = initWasSentences();
        initLoveSentences(wasLove);
        if (loveList.isEmpty()) {
            cleanWasFile();
            initLoveSentences(Set.of());
        }
    }

    public void store(String message) {
        loveList.add(message);
    }

    private Set<String> initWasSentences() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(wasFile, StandardCharsets.UTF_8))) {
            return br.lines().collect(Collectors.toSet());
        }
    }

    private void cleanWasFile() throws IOException {
        new FileOutputStream(wasFile).close();
    }

    private void initLoveSentences(Set<String> wasLove) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(loveFile, StandardCharsets.UTF_8))) {
            loveList = br.lines()
                    .filter(love -> !wasLove.contains(love))
                    .filter(love -> !love.isEmpty())
                    .collect(Collectors.toList());
        }
    }

    public String getRandom() throws IOException {
        int index = random.nextInt(loveList.size());
        String love = loveList.get(index);
        setWasLove(love);
        loveList.remove(index);
        if (loveList.isEmpty()) {
            cleanWasFile();
            initLoveSentences(Set.of());
        }
        return love;
    }

    private void setWasLove(String love) throws IOException {
        Files.writeString(Paths.get(wasFile), love + LINE_SEPARATOR, StandardOpenOption.APPEND);
    }

    public int getSizeLoveList() {
        return loveList.size();
    }

    @Override
    public String toString() {
        return "LoveSentences{" +
                "loveFile='" + loveFile + LINE_SEPARATOR +
                ", wasFile='" + wasFile + LINE_SEPARATOR +
                ", loveList=" + loveList +
                '}';
    }
}
