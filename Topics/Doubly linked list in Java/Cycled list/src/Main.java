import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

class Main {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            String[] n = scanner.nextLine().split("\\s+");
            DoublyLinkedList<Integer> list = new DoublyLinkedList<>();
            Arrays.stream(scanner.nextLine().split("\\s+"))
                  .mapToInt(Integer::parseInt)
                  .forEach(entry -> {
                      if (list.isEmpty()) {
                          list.addFirst(entry);
                      } else {
                          list.addLast(entry);
                      }
                  });
            Node pointer = list.getHead();
            for (int i = 0; i < Integer.parseInt(n[1]); i++) {
                String[] command = scanner.nextLine().split("\\s+");
                if (command[0].equals("r")) {
                    for (int j = 1; j < Integer.parseInt(command[1]); j++) {
                        pointer = pointer.getNext() != null ? pointer.getNext() : list.getHead();
                    }
                    list.remove(pointer.getNext() != null ? pointer.getNext() : list.getHead());
                    pointer = pointer.getNext() != null ? pointer.getNext() : list.getHead();
                } else {
                    for (int j = 1; j < Integer.parseInt(command[1]); j++) {
                        pointer = pointer.getPrev() != null ? pointer.getPrev() : list.getTail();
                    }
                    list.remove(pointer.getPrev() != null ? pointer.getPrev() : list.getTail());
                    pointer = pointer.getPrev() != null ? pointer.getPrev() : list.getTail();
                }
            }
            System.out.print(list);
        }
    }

    public static class Node<E> {

        private final E value;
        private Node<E> next;
        private Node<E> prev;

        Node(E element, Node<E> next, Node<E> prev) {
            this.value = element;
            this.next = next;
            this.prev = prev;
        }

        Node<E> getNext() {
            return next;
        }

        Node<E> getPrev() {
            return prev;
        }
    }

    public static class DoublyLinkedList<E> {

        private Node<E> head;
        private Node<E> tail;
        private int size;

        public DoublyLinkedList() {
            size = 0;
        }

        public Node<E> getHead() {
            return head;
        }

        public Node<E> getTail() {
            return tail;
        }

        public int getSize() {
            return size;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public String toString() {
            Node<E> tmp = head;
            StringBuilder result = new StringBuilder();

            while (tmp != null) {
                result.append(tmp.value).append(" ");
                tmp = tmp.next;
            }
            return result.toString();
        }

        public void addFirst(E elem) {
            Node<E> tmp = new Node<>(elem, head, null);

            if (size == 0) {
                head = tail = tmp;
            } else {
                head.prev = tmp;
                head = tmp;
            }
            size++;
        }

        public void addLast(E elem) {
            Node<E> tmp = new Node<>(elem, null, tail);

            if (size == 0) {
                head = tail = tmp;
            } else {
                tail.next = tmp;
                tail = tmp;
            }
            size++;
        }

        public void add(E elem, Node<E> curr) {
            if (curr == null) {
                throw new NoSuchElementException();
            }
            if (curr.prev == null) {
                addFirst(elem);
                return;
            }

            Node<E> tmp = new Node<>(elem, curr, curr.prev);

            curr.prev.next = tmp;
            curr.prev = tmp;
            size++;
        }

        public void removeFirst() {
            if (size == 0) {
                throw new NoSuchElementException();
            }

            if (size == 1) {
                head = tail = null;
            } else {
                head = head.next;
                head.prev = null;
            }
            size--;
        }

        public void removeLast() {
            if (size == 0) {
                throw new NoSuchElementException();
            }

            if (size == 1) {
                head = tail = null;
            } else {
                tail = tail.prev;
                tail.next = null;
            }
            size--;
        }

        public void remove(Node<E> curr) {
            if (curr == null) {
                throw new NoSuchElementException();
            }

            if (curr.prev == null) {
                removeFirst();
                return;
            }
            if (curr.next == null) {
                removeLast();
                return;
            }

            curr.prev.next = curr.next;
            curr.next.prev = curr.prev;
            size--;
        }
    }
}