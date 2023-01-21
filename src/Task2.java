import java.util.*;

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
//        String testString1 = "yourapp the quick     brown fox jumps    over         \t the lazy dog ";
//        String testString2 = "yourapp the quick brown fox cat dog jumps over the lazy dog cat cat the beaver";
//        String testString3 = "yourapp the quick brown fox cat dog jumps over the lazy dog cat cat the beaver lion the cat cat lion tiger tiger1 tiger2";
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

        Map<String, Integer> wordMap = new HashMap<>();
        for (String word : words) {
            if (word.isBlank() || word.isEmpty()) {
                continue;
            }
            if (wordMap.containsKey(word)) {
                int countWord = wordMap.get(word);
                wordMap.put(word, ++countWord);
            } else {
                wordMap.put(word, 1);
            }
        }
        System.out.printf("В тексте %d слов\n", wordMap.size());

        Map<Integer, Set<String>> wordSortedMap = new TreeMap<>(Comparator.reverseOrder());

        for (Map.Entry<String, Integer> wordCounts : wordMap.entrySet()) {
            String word = wordCounts.getKey();
            Integer countOccurrenceWord = wordCounts.getValue();

            Set<String> wordSet;
            if (wordSortedMap.containsKey(countOccurrenceWord)) {
                wordSet = wordSortedMap.get(countOccurrenceWord);
            } else {
                wordSet = new TreeSet<>();
            }
            wordSet.add(word);
            wordSortedMap.put(countOccurrenceWord, wordSet);
        }

        int i = 0;
        for (Map.Entry<Integer, Set<String>> entry : wordSortedMap.entrySet()) {
            Integer countOccurrenceWord = entry.getKey();
            Set<String> wordSet = entry.getValue();
            for (String word : wordSet) {
                System.out.printf("%d - %s\n", countOccurrenceWord, word);
                i++;
                if (i == 10) {
                    break;
                }
            }
            if (i == 10) {
                break;
            }
        }
    }

}
