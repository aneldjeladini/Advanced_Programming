import java.time.LocalDateTime;
import java.util.*;

interface NewsSubscriber{
    void update(Article article);
    String getUsername();
}

class User implements NewsSubscriber{
    String username;
    Set<Article> articles;

    public User(String username){
        this.username = username;
        this.articles = new HashSet<>();
    }


    @Override
    public void update(Article article) {
        articles.add(article);
    }

    public String getUsername() {
        return username;
    }

    public Set<Article> getArticles() {
        return articles;
    }

}


class Article {

    private final String category;
    private final String author;
    private final String content;
    private final LocalDateTime timestamp;

    public Article(String category, String author, String content, LocalDateTime timestamp) {
        this.category = category;
        this.author = author;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getCategory() {
        return category;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString(){
        return String.format("[%s] %s - %s\n%s",timestamp,author,category,content);
    }
}

class NewsSystem{
    List<String> categoryNames;
    List<String> authorNames;
    Map<String,NewsSubscriber> users = new HashMap<>();
    Map<String,List<NewsSubscriber>> usersByCategory = new HashMap<>();
    Map<String,List<NewsSubscriber>> usersByAuthors = new HashMap<>();

    public NewsSystem(List<String> categoryNames, List<String> authorNames) {
        this.categoryNames = categoryNames;
        this.authorNames = authorNames;
    }

    public void addUser(String username){
        users.putIfAbsent(username,new User(username));
    }

    public void subscribeUserToCategory(String username,String categoryName){
        usersByCategory.putIfAbsent(categoryName,new ArrayList<>());
        if (users.containsKey(username)){
            usersByCategory.get(categoryName).add(users.get(username));
        }
    }

    public void unsubscribeUserFromCategory(String username, String categoryName){
        if (users.containsKey(username)){
            usersByCategory.get(categoryName).remove(users.get(username));
        }
    }

    public void subscribeUserToAuthor(String username, String authorName){
        usersByAuthors.putIfAbsent(authorName,new ArrayList<>());
        if (users.containsKey(username)){
            usersByAuthors.get(authorName).add(users.get(username));
        }
    }

    public void unsubscribeUserFromAuthor(String username,String authorName){
        if (users.containsKey(username)){
            usersByAuthors.get(authorName).remove(users.get(username));
        }
    }

    public void publishArticle(Article article){
        if (usersByAuthors.containsKey(article.getAuthor())){
            usersByAuthors.get(article.getAuthor()).forEach(u -> u.update(article));
        }
        if (usersByCategory.containsKey(article.getCategory())){
            usersByCategory.get(article.getCategory()).forEach(u -> u.update(article));
        }
    }

    public void printNewsForUser(String username){
        if (!users.containsKey(username)){
            return;
        }
        User user = (User) users.get(username);
        StringBuilder sb = new StringBuilder();
        sb.append("News for user: ").append(user.getUsername()).append('\n');
        user.getArticles().stream()
                .sorted(Comparator.comparing(Article::getTimestamp))
                .forEach(a -> sb.append(a).append('\n'));

        sb.setLength(sb.length()-1);
        System.out.println(sb);
    }

}


public class NewsSystemTest {

    public static void main(String[] args) {

        // Hardcoded categories and authors
        List<String> categories = List.of(
                "Technology", "Sports", "Politics", "Health", "Science",
                "Business", "Education", "Culture", "Travel", "Entertainment"
        );

        List<String> authors = List.of(
                "MartinFowler", "JohnDoe", "AliceSmith", "BobBrown", "JaneMiller"
        );

        NewsSystem system = new NewsSystem(categories, authors);

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+", 2);
            String command = parts[0];

            switch (command) {

                case "ADD_USER":
                    system.addUser(parts[1]);
                    break;

                case "SUBSCRIBE_CATEGORY": {
                    String[] p = parts[1].split("\\s+");
                    system.subscribeUserToCategory(p[0], p[1]);
                    break;
                }

                case "UNSUBSCRIBE_CATEGORY": {
                    String[] p = parts[1].split("\\s+");
                    system.unsubscribeUserFromCategory(p[0], p[1]);
                    break;
                }

                case "SUBSCRIBE_AUTHOR": {
                    String[] p = parts[1].split("\\s+");
                    system.subscribeUserToAuthor(p[0], p[1]);
                    break;
                }

                case "UNSUBSCRIBE_AUTHOR": {
                    String[] p = parts[1].split("\\s+");
                    system.unsubscribeUserFromAuthor(p[0], p[1]);
                    break;
                }

                case "PUBLISH": {
                    // format:
                    // PUBLISH <category> <author> <timestamp> <content>
                    String[] p = parts[1].split("\\s+", 4);
                    Article article = new Article(
                            p[0],
                            p[1],
                            p[3],
                            LocalDateTime.parse(p[2])
                    );
                    system.publishArticle(article);
                    break;
                }

                case "PRINT":
                    system.printNewsForUser(parts[1]);
                    break;
            }
        }
    }
}




