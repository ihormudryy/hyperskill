package model;

import DataStructures.DoublyLinkedList;
import DataStructures.Entry;
import com.google.gson.internal.LinkedTreeMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Line {

    private final DoublyLinkedList<Station> stationsList;
    private final String name;

    public Line(String name) {
        this.stationsList = new DoublyLinkedList<>();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addStation(Station station) {
        if (name != null) {
            stationsList.add(station);
        }
    }

    public void connectSiblings(Entry<Station> from, Entry<Station> to, Station st, boolean isNext) {
        ArrayList<?> start = isNext ? st.getNextStations() : st.getPrevStations();
        start.forEach(sibling -> {
            // This hack is done for avoiding data bug with station "Barking" on "District line"
            // where "next" station is an array of objects but not of strings!!!
            // Check https://stepik.org/media/attachments/lesson/373079/london_underground.json
            if (sibling instanceof LinkedTreeMap) {
                //System.out.println("Transfer -> " + sibling);
                LinkedTreeMap tmp = (LinkedTreeMap) sibling;
                st.setTransfer((LinkedTreeMap) sibling);
            }

            if (sibling.equals(to.getValue().getName())) {
                if (isNext) {
                    from.setNext(to);
                } else {
                    from.setPrev(to);
                }
            }
        });
    }

    public void buildLine() {
        Set<Entry<Station>> entries = stationsList.getSetOfEntries();
        entries.forEach(el -> {
            Station from = el.getValue();
            for (Entry<Station> to : entries) {
                connectSiblings(el, to, from, true); // Connect with next stations
                connectSiblings(el, to, from, false); // Connect with prev stations
            }
        });
        entries.forEach(el -> {
            if (el.getNext() == null) {
                stationsList.setTail(el);
            }
            if (el.getPrev() == null) {
                stationsList.setHead(el);
            }
        });
    }

    public Station getNextStationAfter(String thisStation) {
        return (Station) stationsList.getListOfEntries()
                                     .stream()
                                     .map(s -> {
                                         if (Objects.equals(s.getValue().getName(), thisStation)) {
                                             return s.getNext() != null ? s.getNext().getValue() : null;
                                         }
                                         return null;
                                     })
                                     .filter(Objects::nonNull)
                                     .findFirst().orElse(null);
    }

    public Station getPrevStationBefore(String thisStation) {
        return (Station) stationsList.getListOfEntries()
                                     .stream()
                                     .map(s -> {
                                         if (Objects.equals(s.getValue().getName(), thisStation)) {
                                             return s.getPrev() != null ? s.getPrev().getValue() : null;
                                         }
                                         return null;
                                     })
                                     .filter(Objects::nonNull)
                                     .findFirst()
                                     .orElse(null);
    }

    public void addHeadStation(Station station) {
        stationsList.addHead(station);
    }

    public void removeStation(String name) {
        stationsList.getListOfElements().forEach(station -> {
            if (Objects.equals(station.getName(), name)) {
                stationsList.remove(station);
            }
        });
    }

    public Station getStation(String name) {
        List<Entry<Station>> entries = stationsList.getListOfEntries();
        return entries.stream()
                      .map(s -> {
                          if (Objects.equals(s.getValue().getName(), name)) {
                              return s.getValue();
                          }
                          return null;
                      })
                      .filter(Objects::nonNull)
                      .findFirst()
                      .orElse(null);
    }

    @Override
    public String toString() {
        return stationsList.getListOfEntries()
                           .stream()
                           .map(current -> current.getValue().toString())
                           .collect(Collectors.joining("\n", "depot\n", "\ndepot"));
    }
}
