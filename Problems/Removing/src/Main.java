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
            if (value == null) {
                remove(key);
                return false;
            }
            int idx = findKey(key);
            table[idx] = new TableEntry(key, value);
            return true;
        }

        public void remove(int key) {
            int idx = get(key);
            if (idx != -1) {
                table[idx].remove();
            }
        }

        public int get(int key) {
            for (int i = 0; i < table.length; i++) {
                if (table[i] != null && table[i].getKey() == key) {
                    return i;
                }
            }
            return -1;
        }

        private int findKey(int key) {
            int hash = key % size;

            while (!(table[hash] == null || table[hash].getKey() == key)) {
                hash = (hash + 1) % size;

                if (hash == key % size) {
                    rehash();
                    hash = key % size;
                }
            }

            return hash;
        }

        private void rehash() {
            int s = table.length * 2;
            TableEntry[] new_table = new TableEntry[s];
            for (int t = 0; t < table.length; t++) {
                int new_key = table[t].getKey() % s;
                while (new_table[new_key] != null) {
                    new_key++;
                }
                new_table[new_key] = table[t];
            }
            this.table = new_table;
            this.size = s;
        }

        @Override
        public String toString() {
            StringBuilder tableStringBuilder = new StringBuilder();

            for (int i = 0; i < table.length; i++) {
                if (table[i] == null) {
                    tableStringBuilder.append(i + ": null");
                } else {
                    tableStringBuilder.append(i + ": key=" + table[i].getKey()
                                                + ", value=" + table[i].getValue()
                                                + ", removed=" + table[i].isRemoved());
                }

                if (i < table.length - 1) {
                    tableStringBuilder.append("\n");
                }
            }

            return tableStringBuilder.toString();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] line = scanner.nextLine().split("\\s+");
        HashTable<String> table = new HashTable<>(5);
        for (int i = 0; i < Integer.valueOf(line[0]); i++) {
            String[] commands = scanner.nextLine().split("\\s+");
            table.put(Integer.valueOf(commands[0]), commands.length > 1 ? commands[1] : null);
        }

        for (int i = 0; i < Integer.valueOf(line[1]); i++) {
            String[] commands = scanner.nextLine().split("\\s+");
            table.remove(Integer.valueOf(commands[0]));
        }
        System.out.println(table.toString());
    }
}