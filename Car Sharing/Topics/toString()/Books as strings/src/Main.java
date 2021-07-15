import java.util.Arrays;
import java.util.stream.Collectors;

class Book {

    private String title;
    private int yearOfPublishing;
    private String[] authors;

    public Book(String title, int yearOfPublishing, String[] authors) {
        this.title = title;
        this.yearOfPublishing = yearOfPublishing;
        this.authors = authors;
    }

    @Override
    public String toString() {
        return "title=" + title + ",yearOfPublishing=" + yearOfPublishing +
                ",authors=[" + Arrays.stream(authors).collect(Collectors.joining(",")) + "]";
    }

//    public static void main(String[] args) {
//        Book book = new Book("Some title", 2002, new String[]{"Hello", "Fresh"});
//        System.out.println(book.toString());
//    }
}