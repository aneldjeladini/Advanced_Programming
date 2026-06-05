import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class TermFrequency {
    String[] words;
    Set<String> distinctWords;
    Map<String, Integer> wordOccurrence;

    public TermFrequency(InputStream inputStream, String[] stopWords) {
        Set<String> stopSet = Arrays.stream(stopWords)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        Scanner scanner = new Scanner(inputStream);
        List<String> wordList = new ArrayList<>();

        while (scanner.hasNext()) {
            String token = scanner.next()
                    .toLowerCase()
                    .replaceAll("[.,]", "");

            if (token.isEmpty() || stopSet.contains(token)) {
                continue;
            }
            wordList.add(token);
        }

        this.words = wordList.toArray(new String[0]);

        this.distinctWords = new HashSet<>(Arrays.asList(words));

        this.wordOccurrence = new TreeMap<>();
        for (String word : words) {
            wordOccurrence.merge(word, 1, Integer::sum);
        }
    }

    public int countTotal() {
        return words.length;
    }

    public int countDistinct() {
        return distinctWords.size();
    }

    public List<String> mostOften(int k) {
        return wordOccurrence.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(k)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}

public class TermFrequencyTest {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        String[] stop = new String[]{"во", "и", "се", "за", "ќе", "да", "од",
                "ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
                "што", "на", "а", "но", "кој", "ја"};
        TermFrequency tf = new TermFrequency(System.in, stop);
        System.out.println(tf.countTotal());
        System.out.println(tf.countDistinct());
        System.out.println(tf.mostOften(10));
    }
}