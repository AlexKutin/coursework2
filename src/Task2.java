import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Task2 {

    // Напишите приложение, которое на вход через консоль, получит текст и выдаст статистику:
    // 1. Количество слов в тексте
    // 2. TOP10 самых часто упоминаемых слов, упорядоченных по количеству упоминаний в
    // обратном порядке. В случае одинакового количества упоминаний слова должны быть отсортированы по алфавиту.
    // Например:
    //**Input:**
    // yourapp the quick brown fox jumps over the lazy dog
    // **Output:**
    // В тексте 9 слов
    // TOP10:
    // 2 — the
    // 1 — brown
    // 1 — dog
    // 1 — fox
    // 1 — jumps
    // 1 — lazy
    // 1 — over
    // 1 — quick

    public static void main(String[] args) {
//        String testString1 = "yourapp the quick     brown fox jumps    over         \\t the lazy dog ";
//        String testString2 = "yourapp the quick brown fox cat dog jumps over the lazy dog cat cat the beaver";
//        String testString3 = "yourapp the quick brown fox cat dog jumps over the lazy dog cat cat the beaver lion the cat cat lion tiger tiger1 tiger2";
//        String testString4 = "yourApp tHe quick brown fox Cat dog jumps over the lazy dog CAT cat the beaver LIon the cat cat lion tiger tiger1 tiger2";
//        analyzeText(testString1);
//        System.out.println("--------------");
//        analyzeText(testString2);
//        System.out.println("--------------");
//        analyzeText(testString3);
//        Test strings passed

        Scanner scanner = new Scanner(System.in);
        String input;
        while (true) {
            System.out.println("Введите текстовую строку для анализа (exit - для выхода):");
            input = scanner.nextLine();
            if ("exit".equals(input)) {
                break;
            } else {
                analyzeText(input);
            }
        }
        scanner.close();
    }

    public static void analyzeText(String text) {
        String[] words = text.split(" ");

        Map<String, Long> wordMap = Stream
                .of(words)
                .filter((s) -> !(s.isEmpty() || s.isBlank()))
                .map(String::toLowerCase)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        System.out.printf("В тексте %d слов\n", wordMap.size());
        wordMap.entrySet()
               .stream()
               .sorted(Comparator.comparing(Map.Entry<String, Long>::getValue).reversed()
                       .thenComparing(Map.Entry::getKey))
               .limit(10).forEach(e -> System.out.printf("%d - %s\n", e.getValue(), e.getKey()));
    }
}
