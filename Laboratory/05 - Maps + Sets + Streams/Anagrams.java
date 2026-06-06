import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Anagrams {



    public static void main(String[] args) throws IOException {
        findAll(System.in);
    }


    public static boolean areAnagrams(String base, String other){
        if (base.length() != other.length()) return false;
        if (base.equals(other)) return true;

        List<Character> chars  = new ArrayList<>();

        for (char c : other.toCharArray()){
            chars.add(c);
        }

        for (char c : base.toCharArray()){
            if (!chars.remove(Character.valueOf(c))){
                return false;
            }
        }

        return chars.isEmpty();
    }

    public static void findAll(InputStream inputStream) throws IOException {
        BufferedReader br  = new BufferedReader(new InputStreamReader(inputStream));
        Map<String,List<String>> anagrams = new TreeMap<>();
        Set<List<String>> totalanagrams = new LinkedHashSet<>();
        while (true){
            String word = br.readLine();
            if (word == null || word.isEmpty()) break;
            anagrams.putIfAbsent(word,new ArrayList<>());
        }

        for (String word : anagrams.keySet()){
            for (String compareWord : anagrams.keySet()){
                if (areAnagrams(word,compareWord)){
                    anagrams.get(word).add(compareWord);
                }
            }
        }

        anagrams.values()
                .stream()
                .filter(a -> a.size() >= 5)
                .forEach(totalanagrams::add);

        totalanagrams.forEach(line -> {
                    StringBuilder sb = new StringBuilder();
                    for (String a : line){
                        sb.append(a).append(" ");
                    }
                    sb.setLength(sb.length()-1);
                    System.out.println(sb);
                });

    }
}
