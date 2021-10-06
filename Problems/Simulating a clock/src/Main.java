class Clock {

    int hours = 12;
    int minutes = 0;

    void next() {
        hours = minutes + 1 > 59 ? hours + 1 > 12 ? 1 : ++hours : hours;
        minutes = ++minutes % 60;
    }
}