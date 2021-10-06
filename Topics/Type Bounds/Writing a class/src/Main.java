class Shelf<T extends Book> {

    T element;

    Shelf(T element) {
        this.element = element;
    }

    T getElement() {
        return element;
    }

    void setElement(T element) {
        this.element = element;
    }
}