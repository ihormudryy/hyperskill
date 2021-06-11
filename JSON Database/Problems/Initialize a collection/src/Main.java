import java.util.ArrayList;

class Main {
    ArrayList<String> list = new ArrayList<>();

    void init() {
        list.add("one");
        list.add("two");
        list.add("three");
    }
}

class Test {
    public static void main(String[] args) {
        Main main = new Main();
        main.init();

        for (String str : main.list) {
            System.out.println(str);
        }
    }
}