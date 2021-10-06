package DataStructures;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class DoublyLinkedList<T> {

    private Entry head = null;
    private Entry tail = null;
    private Set<Entry<T>> set;

    public DoublyLinkedList() {
        this.set = new HashSet<>();
    }

    public void add(T value) {
        Entry<T> entry = new Entry(value);
        set.add(entry);
    }

    public void setHead(Entry<T> entry) {
        head = entry;
    }

    public void setTail(Entry<T> entry) {
        tail = entry;
    }

    public void addHead(T value) {
        Entry<T> entry = new Entry(value);
        if (head == null && tail == null) {
            head = entry;
            tail = entry;
        } else {
            entry.setNext(head);
            head.setPrev(entry);
            head = entry;
        }
        set.add(entry);
    }

    public void addTail(T value) {
        Entry<T> entry = new Entry(value);
        if (head == null) {
            this.head = entry;
        } else {
            this.tail.setNext(entry);
            entry.setPrev(this.tail);
        }
        this.tail = entry;
        set.add(entry);
    }

    public void remove(T element) {
        Entry<T> index = head;
        while (index != null && element != null) {
            if (Objects.equals(index.getValue(), element)) {
                if (index.getPrev() != null) {
                    index.getPrev().setNext(index.getNext()); // Connect previous node with the next one
                } else {
                    head = head.getNext();
                }
                if (index.getNext() != null) {
                    index.getNext().setPrev(index.getPrev()); // Connect next node with previous
                } else {
                    tail = tail.getPrev();
                }
                set.remove(index);
                return;
            }
            index = index.getNext();
            if (index == head) {
                // If this is looped list and we reached the head then exit
                return;
            }
        }
    }

    public LinkedList<T> getListOfElements() {
        LinkedList<T> list = new LinkedList<>();
        Entry<T> pointer = head;
        Set<Entry<T>> visited = new HashSet<>();
        visited.add(pointer);
        while (pointer != null) {
            list.add(pointer.getValue());
            pointer = pointer.getNext();
            if (visited.contains(pointer)) {
                return list;
            } else {
                visited.add(pointer);
            }
        }
        return list;
    }

    public List<Entry<T>> getListOfEntries() {
        LinkedList<Entry<T>> list = new LinkedList<>();
        Entry<T> pointer = head;
        Set<Entry<T>> visited = new HashSet<>();
        visited.add(pointer);
        while (pointer != null) {
            list.add(pointer);
            pointer = pointer.getNext();
            if (visited.contains(pointer)) {
                return list;
            } else {
                visited.add(pointer);
            }
        }
        return list;
    }

    public Set<Entry<T>> getSetOfEntries() {
        return set;
    }
}
