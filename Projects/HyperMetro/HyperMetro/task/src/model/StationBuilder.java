package model;

import com.google.gson.internal.LinkedTreeMap;
import java.util.ArrayList;

public class StationBuilder {

    private Double time;
    private String name;
    private ArrayList<?> next;
    private ArrayList<?> prev;
    private ArrayList<LinkedTreeMap> transfers;

    public StationBuilder() {
    }

    public StationBuilder setTime(Double time) {
        this.time = time;
        return this;
    }

    public StationBuilder setName(String name) {
        this.name = name.replace(",", ".");
        return this;
    }

    public StationBuilder setNextStation(ArrayList<?> nextStation) {
        if (nextStation.size() > 1) {
            System.out.println(this.name + " next: " + nextStation);
        }
        this.next = nextStation;
        return this;
    }

    public StationBuilder setPrevStation(ArrayList<?> prevStation) {
        if (prevStation.size() > 1) {
            System.out.println(this.name + " prevs: " + prevStation);
        }
        this.prev = prevStation;
        return this;
    }

    public StationBuilder setTransfers(ArrayList<LinkedTreeMap> transfers) {
        this.transfers = transfers;
        return this;
    }

    public Station build() {
        return new Station(name, transfers, time, next, prev);
    }
}
