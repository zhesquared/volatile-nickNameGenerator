import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    static AtomicInteger threeSymWords = new AtomicInteger(0);
    static AtomicInteger fourSymWords = new AtomicInteger(0);
    static AtomicInteger fiveSymWords = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {

        final ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        final List<Callable<Boolean>> threads = new ArrayList<>();

        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        threads.add(() -> {
            for (String text : texts) {
                if (isPalindrome(text)) {
                    switcher(text.length());
                }
            }
            return true;
        });

        threads.add(() -> {
            for (String text : texts) {
                if (isRepeat(text)) {
                    switcher(text.length());
                }
            }
            return true;
        });

        threads.add(() -> {
            for (String text : texts) {
                if (isAscending(text)) {
                    switcher(text.length());
                }
            }
            return true;
        });

        threadPool.invokeAll(threads);
        threadPool.shutdown();

        System.out.printf("Красивых слов с длиной 3: %s шт \n", threeSymWords);
        System.out.printf("Красивых слов с длиной 4: %s шт \n", fourSymWords);
        System.out.printf("Красивых слов с длиной 5: %s шт \n", fiveSymWords);
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static boolean isPalindrome(String text) {
        StringBuilder reverser = new StringBuilder();
        return reverser.append(text).reverse().toString().equals(text);
    }

    public static boolean isRepeat(String text) {
        return text.replace(text.charAt(0), ' ').isBlank();
    }

    public static boolean isAscending(String text) {
        int count = 0;
        char[] symbols = text.toCharArray();
        for (int i = 0; i < symbols.length - 1; i++) {
            if (symbols[i] <= symbols[i + 1]) {
                count++;
            }
        }
        return count == text.length() - 1;
    }

    public static void switcher(int count) {
        switch (count) {
            case 3 -> threeSymWords.addAndGet(1);
            case 4 -> fourSymWords.addAndGet(1);
            case 5 -> fiveSymWords.addAndGet(1);
        }
    }
}