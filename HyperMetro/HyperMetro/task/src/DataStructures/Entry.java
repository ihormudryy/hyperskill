package DataStructures;

public class Entry<T> {

    private Entry prev = null;
    private Entry next = null;
    private T value;

    public Entry(T value) {
        this.value = value;
    }

    public Entry getPrev() {
        return prev;
    }

    public void setPrev(Entry prev) {
        this.prev = prev;
    }

    public Entry getNext() {
        return next;
    }

    public void setNext(Entry next) {
        this.next = next;
    }

    public T getValue() {
        return value;
    }
}
