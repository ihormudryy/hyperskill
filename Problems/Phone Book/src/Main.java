import java.util.Scanner;

public class Main {
    private static class TableEntry<T> {
        private final int key;
        private final T value;
        private boolean removed;

        public TableEntry(int key, T value) {
            this.key = key;
            this.value = value;
        }

        public int getKey() {
            return key;
        }

        public T getValue() {
            return value;
        }

        public void remove() {
             removed = true;   
        }

        public boolean isRemoved() {
             return removed;   
        }
    }

    private static class HashTable<T> {
        private int size;
        private TableEntry[] table;

        public HashTable(int size) {
            this.size = size;
            table = new TableEntry[size];
        }

        public boolean put(int key, T value) {
            TableEntry entry = new TableEntry(key, value);
            table[key % size] = entry;
            return table[key % size].isRemoved();
        }

        public T get(int key) {

            return (table[key % size] == null || table[key % size].isRemoved()) ? null :
                (T) table[key % size].getValue();
        }

        public void remove(int key) {

            if (table[key % size] != null) {
                table[key % size].remove();
            }
        }

        private int findKey(int key) {
            int hash = key % size;

            while (!(table[hash] == null || table[hash].getKey() == key)) {
                hash = (hash + 1) % size;

                if (hash == key % size) {
                    return -1;
                }
            }

            return hash;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        HashTable<String> table = new HashTable<>(scanner.nextInt());
        while (scanner.hasNext()) {
            String[] commands = scanner.nextLine().split("\\s+");
            switch (commands[0]) {
                case "put":
                    table.put(Integer.valueOf(commands[1]), commands[2]);
                    break;
                case "get":
                    String result = table.get(Integer.valueOf(commands[1]));
                    System.out.println(result == null ? -1 : result);
                    break;
                case "remove":
                    table.remove(Integer.valueOf(commands[1]));
                    break;
            }
        }
    }
}