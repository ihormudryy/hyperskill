package model;

import com.google.gson.internal.LinkedTreeMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

public class Station {

    private final Integer time;
    private final String name;
    private final LinkedHashMap<String, String> transfers;
    private ArrayList<?> nextStation;
    private ArrayList<?> prevStation;

    Station(String name,
            ArrayList<LinkedTreeMap> transfers,
            Double time,
            ArrayList<?> nextStation,
            ArrayList<?> prevStation
    ) {

        this.name = name;
        this.transfers = new LinkedHashMap<>();
        this.time = time != null ? time.intValue() : null;
        this.nextStation = nextStation;
        this.prevStation = prevStation;

        try {
            transfers.forEach(transfer -> {
                this.setTransfer(transfer);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Integer getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public LinkedHashMap<String, String> getTransfers() {
        return transfers;
    }

    public void setTransfer(String line, String station) {
        if (line.contains("Jubilee") && !line.contains("line")) {
            // F...g hack for corrupted input data on station Jubelee
            line += " line";
        }
        this.transfers.put(line.replace(",", "."), station.replace(",", "."));
    }

    public void setTransfer(LinkedTreeMap transfer) {
        String line = (String) transfer.get("line");
        String station = (String) transfer.get("station");
        if (line.contains("Jubilee") && !line.contains("line")) {
            // F...g hack for corrupted input data on station Jubelee
            line += " line";
        }
        this.transfers.put(line.replace(",", "."), station.replace(",", "."));
    }

    public ArrayList<?> getNextStations() {
        return nextStation;
    }

    public ArrayList<?> getPrevStations() {
        return prevStation;
    }

    public String toString() {
        return this.name + this.transfers.keySet()
                                         .stream()
                                         .map(t -> String.format(" - %s (%s line)", transfers.get(t), t))
                                         .collect(Collectors.joining(""));
    }
}
