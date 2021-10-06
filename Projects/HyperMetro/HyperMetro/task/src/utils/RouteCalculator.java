package utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import model.Line;
import model.Metro;
import model.Station;
import model.Tuple;

public class RouteCalculator {

    private Metro grid;
    private String fromLine;
    private String fromStation;
    private String toLine;
    private String toStation;
    private Set<String> visitedStations;
    private LinkedList<Tuple> processingQueue;
    private TreeMap<Integer, String> calculatedRoutes;

    public RouteCalculator(Metro grid) {
        this.grid = grid;
        this.visitedStations = new LinkedHashSet<>();
        this.processingQueue = new LinkedList<>();
    }

    private boolean isVisited(Line line, Station station) {
        if (station != null) {
            return visitedStations.contains(String.format("%s <-> %s", line.getName(), station.getName()));
        } else {
            return true;
        }
    }

    private void markAsVisited(Line line, Station station) {
        visitedStations.add(String.format("%s <-> %s", line.getName(), station.getName()));
    }

    private boolean isDestinationPoint(Line line, Station station) {

        if (grid.getLine(toLine) == null
            || grid.getLine(toLine).getStation(toStation) == null) {
            return false;
        }

        return grid.getLine(toLine)
                   .getName()
                   .equals(line.getName())
            && grid.getLine(toLine).getStation(toStation)
                   .getName()
                   .equals(station.getName());
    }

    private void addStation(Tuple stationTuple, boolean isPrioritized) {
        if (stationTuple.getStation() != null
            && !isVisited(stationTuple.getLine(), stationTuple.getStation())) {

            processingQueue.add(stationTuple);
            if (isPrioritized) {
                Collections.sort(processingQueue, new Comparator<Tuple>() {
                    @Override
                    public int compare(Tuple o1, Tuple o2) {
                        int time1 = o1.getStation().getTime() == null ? 0 : o1.getStation().getTime();
                        int time2 = o2.getStation().getTime() == null ? 0 : o2.getStation().getTime();
                        return time1 <= time2 ? -1 : 1;
                    }
                });
            }
            markAsVisited(stationTuple.getLine(), stationTuple.getStation());
        }
    }

    private boolean isDestinationStation(Tuple station) {
        if (station.getStation() != null) {
            //System.out.println(station.getLine().getName() + " " + station.getStation().getName());
            return station.getStation() != null ? isDestinationPoint(station.getLine(), station.getStation()) : false;
        }
        return false;
    }

    public RouteCalculator setRouteFrom(String startLine,
                                        String startStation) {
        this.fromLine = startLine;
        this.fromStation = startStation;
        return this;
    }

    public RouteCalculator setRouterTo(String endLine,
                                       String endStation) {
        this.toLine = endLine;
        this.toStation = endStation;
        return this;
    }

    public String getShortestRoute() {
        return this.getRoute(false);
    }

    public String getFastestRoute() {
        return this.getRoute(true);
    }

    private String getRoute(boolean isPrioritized) {

        // Reset all the cache
        this.visitedStations = new LinkedHashSet<>();
        this.processingQueue = new LinkedList<>();
        this.calculatedRoutes = new TreeMap<>();

        //Initialize all the objects
        Line startingLine = grid.getLine(this.fromLine);
        Station startingStation = startingLine.getStation(this.fromStation);
        if (startingStation == null) {
            return "[ERROR] Starting station is not found";
        }
        Tuple startingPoint = new Tuple(startingStation, startingLine, null, false);
        HashMap<Station, Line> startCouple = new HashMap<>();
        this.markAsVisited(startingLine, startingStation);
        startCouple.put(startingStation, startingLine);
        processingQueue.add(startingPoint);

        while (!processingQueue.isEmpty()) {
            Tuple head = processingQueue.pop();
            Line headLine = head.getLine();
            Station headStation = head.getStation();
            LinkedHashMap<String, String> transfers = headStation.getTransfers();
            Station nextStation = headLine.getNextStationAfter(headStation.getName());
            Station prevStation = headLine.getPrevStationBefore(headStation.getName());
            for (String lineName : headStation.getTransfers()
                                              .keySet()
                                              .stream()
                                              .filter(key -> {
                                                  return !isVisited(grid.getLine(key),
                                                                    grid.getLine(key)
                                                                        .getStation(transfers.get(key)));
                                              })
                                              .collect(Collectors.toList())) {

                Line transferLine = grid.getLine(lineName);
                Station transferStation = transferLine.getStation(transfers.get(transferLine.getName()));
                Tuple stationTuple = new Tuple(transferStation, transferLine, head, true);
                addStation(stationTuple, isPrioritized);
                if (isDestinationStation(stationTuple)) {
                    return stationTuple.getRoute(isPrioritized);
                }
            }


            Tuple next = new Tuple(nextStation, headLine, head, false);
            addStation(next, isPrioritized);
            if (isDestinationStation(next)) {
                return next.getRoute(isPrioritized);
            }

            Tuple prev = new Tuple(prevStation, headLine, head, false);
            addStation(prev, isPrioritized);
            if (isDestinationStation(prev)) {
                return prev.getRoute(isPrioritized);
            }
        }

        return "[INFO] Route not found!";
    }
}
