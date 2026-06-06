import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

// todo: implement the necessary classes

class Author{
    private String name;
    private Set<Book> booksWritten;
    private int numBorrows = 0;

    public Author(String name){
        this.name = name;
        this.booksWritten = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public Set<Book> getBooksWritten() {
        return booksWritten;
    }

    public int getNumBorrows() {
        return numBorrows;
    }

    public void addBook(Book book){
        booksWritten.add(book);
    }

    public void borrowBook(){
        numBorrows++;
    }

    @Override
    public String toString(){
        return String.format("%s - %d",name,numBorrows);
    }
}

class Book{
    private String isbn;
    private String title;
    private Author author;
    private int releaseYear;
    private int timesBorrowed = 0;

    public Book(String isbn, String title,Author author,int releaseYear){
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.releaseYear = releaseYear;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public Author getAuthor() {
        return author;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public int getTimesBorrowed() {
        return timesBorrowed;
    }

    public void getBorrowed(){
        timesBorrowed++;
    }

    @Override
    public String toString(){
        return String.format("%s - \"%s\" by %s (%d)",isbn,title,author.getName(),releaseYear);
    }
}

class Member{
    private String id;
    private String name;
    private List<Book> borrowedBooks;
    private int totalBorrows = 0;

    public Member(String id,String name){
        this.id = id;
        this.name = name;
        this.borrowedBooks = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public int getTotalBorrows() {
        return totalBorrows;
    }

    public void borrowBook(Book book){
        borrowedBooks.add(book);
        totalBorrows++;
    }

    public void returnBook(Book book){
        borrowedBooks.remove(book);
    }

    @Override
    public String toString(){
        return String.format("%s (%s) - borrowed now: %d, total borrows: %d",name,id,borrowedBooks.size(),totalBorrows);
    }
}


class LibrarySystem{
    private String name;
    Map<String,Book> books;
    Map<String,Integer> booksInStock;
    Map<String,Member> members;
    Map<String,Author> authors;
    Map<String,List<Member>> waitingList;

    public LibrarySystem(String name){
        this.name = name;
        this.books = new HashMap<>();
        this.booksInStock = new HashMap<>();
        this.members = new HashMap<>();
        this.authors = new TreeMap<>();
        this.waitingList = new HashMap<>();
    }

    public void registerMember(String id, String fullName){
        members.putIfAbsent(id,new Member(id,fullName));
    }

    public void addBook(String isbn, String title,String author,int year){
        Author newAuthor = new Author(author);
        Book book = new Book(isbn,title,newAuthor,year);
        if (!authors.containsKey(author)){
            authors.put(author,newAuthor);
        }

        if (!books.containsKey(isbn)){
            books.put(isbn,book);
            newAuthor.addBook(book);
            booksInStock.put(isbn,1);
        }
        else{
            booksInStock.put(isbn,booksInStock.get(isbn)+1);
        }
    }

    public void borrowBook(String memberId,String isbn){
        if (!books.containsKey(isbn)) return;
        if (!members.containsKey(memberId)) return;

        if (booksInStock.get(isbn) == 0){
            if (!waitingList.containsKey(isbn)){
                waitingList.put(isbn,new ArrayList<>());
            }
            waitingList.get(isbn).add(members.get(memberId));
            return;
        }

        members.get(memberId).borrowBook(books.get(isbn));
        booksInStock.put(isbn,booksInStock.get(isbn)-1);
        authors.get(books.get(isbn).getAuthor().getName()).borrowBook();
        books.get(isbn).getBorrowed();

    }

    public void returnBook(String memberId, String isbn){
        members.get(memberId).returnBook(books.get(isbn));
        booksInStock.put(isbn,booksInStock.get(isbn)+1);
        if (waitingList.containsKey(isbn)){
            if (!waitingList.get(isbn).isEmpty()){
                borrowBook(waitingList.get(isbn).get(0).getId(),isbn);
                waitingList.get(isbn).remove(0);
            }
        }
    }

    public void printMembers(){
        members.values()
                .stream()
                .sorted(Comparator.comparing((Member m) -> m.getBorrowedBooks().size(),Comparator.reverseOrder()).thenComparing(Member::getName))
                .forEach(System.out::println);
    }

    public void printBooks(){
        books.values()
                .stream()
                .sorted(Comparator.comparing(Book::getTimesBorrowed,Comparator.reverseOrder()).thenComparing(Book::getReleaseYear))
                .forEach(b -> System.out.println(b + ", available: " + booksInStock.get(b.getIsbn()) + ", total borrows: " + b.getTimesBorrowed()));
    }

    public void printBookCurrentBorrowers(String isbn){
        StringBuilder sb = new StringBuilder();
        members.values()
                .stream()
                .filter(m -> m.getBorrowedBooks().contains(books.get(isbn)))
                .map(Member::getId)
                .sorted()
                .forEach(m -> sb.append(m).append(", "));

        sb.setLength(sb.length()-2);
        System.out.println(sb);
    }

    public void printTopAuthors(){
        authors.values()
                .stream()
                .sorted(Comparator.comparing(Author::getNumBorrows,Comparator.reverseOrder()).thenComparing(Author::getName))
                .forEach(System.out::println);
    }


    // ========================= ADDITIONAL METHODS ========================= //

    public Map<Book,Integer> getBooksAndNumberOfBorrowings(){
        Map<Book,Integer> bookMap = new TreeMap<>(Comparator.comparing(Book::getTitle));
        books.values().forEach(book -> bookMap.put(book,book.getTimesBorrowed()));
        return bookMap;
    }

    public Map<String, TreeSet<String>> getAuthorsWithBooks(){
        Map<String,TreeSet<String>> authorMap = new TreeMap<>();
        authors.values().forEach(author -> authorMap.put(author.getName(),author.getBooksWritten()
                .stream()
                .map(Book::getIsbn)
                .collect(Collectors.toCollection(TreeSet::new))));
        return authorMap;
    }

    // ====================================================================== //

}

public class LibraryTester {
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            String libraryName = br.readLine();
            //   System.out.println(libraryName); //test
            if (libraryName == null) return;

            libraryName = libraryName.trim();
            LibrarySystem lib = new LibrarySystem(libraryName);

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.equals("END")) break;
                if (line.isEmpty()) continue;

                String[] parts = line.split(" ");

                switch (parts[0]) {

                    case "registerMember": {
                        lib.registerMember(parts[1], parts[2]);
                        break;
                    }

                    case "addBook": {
                        String isbn = parts[1];
                        String title = parts[2];
                        String author = parts[3];
                        int year = Integer.parseInt(parts[4]);
                        lib.addBook(isbn, title, author, year);
                        break;
                    }

                    case "borrowBook": {
                        lib.borrowBook(parts[1], parts[2]);
                        break;
                    }

                    case "returnBook": {
                        lib.returnBook(parts[1], parts[2]);
                        break;
                    }

                    case "printMembers": {
                        lib.printMembers();
                        break;
                    }

                    case "printBooks": {
                        lib.printBooks();
                        break;
                    }

                    case "printBookCurrentBorrowers": {
                        lib.printBookCurrentBorrowers(parts[1]);
                        break;
                    }

                    case "printTopAuthors": {
                        lib.printTopAuthors();
                        break;
                    }

                    default:
                        break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
