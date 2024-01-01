import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import com.google.gson.*;
import com.google.gson.reflect.*;

public class MBTA {

  // Passenger Cache
  public static Map<String, Passenger> Pcache = new HashMap<>();
  // Train Cache
  public static Map<String, Train> Tcache = new HashMap<>();

  // MBTA Information
  public Map<Train, List<Station>> lines = new HashMap<>();
  public Map<Passenger, List<Station>> journeys = new HashMap<>();

  // Passenger Information
  public Map<Passenger, Station> passengersAtStations = new HashMap<>();
  public Map<Passenger, Train> passengersOnTrains = new HashMap<>();

  // Train Information
  public Map<Train, Station> trainsLocations = new HashMap<>();
  public Map<Train, Direction> trainDirection = new HashMap<>();
  public enum Direction {
    INBOUND, OUTBOUND
  }

  // Station Information
  public Map<Station, Train> trainsAtStation = new HashMap<>();

  // Creates an initially empty simulation
  public MBTA() { }

  public synchronized void board(Passenger p, Station s, Train t) {
    passengersAtStations.remove(p);
    passengersOnTrains.put(p, t);
    journeys.get(p).remove(0);
  }

  public synchronized void deboard(Passenger p, Station s, Train t) {
    passengersOnTrains.remove(p);
    passengersAtStations.put(p, s);
  }

  public boolean arrived(Passenger p) {
    List<Station> journey = journeys.get(p);
    return passengersAtStations.get(p) == journey.get(journey.size() - 1);
  }

  public Station destination(Passenger p) {
    return journeys.get(p).get(0);
  }

  public boolean simulate() {
    for (Passenger p : journeys.keySet()) {
      if (!arrived(p)) { return true; }
    }
    return false;
  }

  public Train getTrain(Passenger p) {
    Station currentStation = passengersAtStations.get(p);
    Station destinationStation = journeys.get(p).get(1);
    for (Map.Entry<Train, List<Station>> line : lines.entrySet()) {
      Train train = line.getKey();
      List<Station> stations = line.getValue();
      if (stations.contains(currentStation) && stations.contains(destinationStation)) {
        return train;
      }
    }
    throw new RuntimeException("Invalid Journey");
  }

  public Station nextStation(Train t) {
    Station src = trainsLocations.get(t);
    List<Station> line = lines.get(t);
    int i;
    for (i = 0; i < line.size(); i++) {
      if (line.get(i) == src) {
        break;
      }
    }
    Direction dir = trainDirection.get(t);
    if (dir == Direction.OUTBOUND) {
      i++;
    } else {
      i--;
    }
    Station next = line.get(i);
    return next;
  }

  public synchronized void moveTrain(Train t, Station src, Station dst) {
    // Update the trains location
    trainsLocations.remove(t);
    trainsLocations.put(t, dst);
    // Update the stations status
    trainsAtStation.remove(src);
    trainsAtStation.put(dst, t);
    // Update the trains direction of movement 
    List<Station> line = lines.get(t);
    if (line.get(0) == dst) {
      trainDirection.remove(t);
      trainDirection.put(t, Direction.OUTBOUND);
    } else if (line.get(line.size() - 1) == dst) {
      trainDirection.remove(t);
      trainDirection.put(t, Direction.INBOUND);
    }
  }

  // Adds a new transit line with given name and stations
  public void addLine(String name, List<String> stations) {
    Train line = Train.make(name);
    List<Station> ListOfStations = new ArrayList<>();
    for (String station : stations) {
      Station s = Station.make(station);
      ListOfStations.add(s);
    }
    lines.put(line, ListOfStations);

    // Trains start at the first station
    trainsLocations.put(line, ListOfStations.get(0));
    trainDirection.put(line, Direction.OUTBOUND);
    trainsAtStation.put(ListOfStations.get(0), line);
  }

  // Adds a new planned journey to the simulation
  public void addJourney(String name, List<String> stations) {
    Passenger passenger = Passenger.make(name);
    List<Station> ListOfStations = new ArrayList<>();
    for (String station : stations) {
      Station s = Station.make(station);
      ListOfStations.add(s);
    }
    journeys.put(passenger, ListOfStations);

    // Passengers start at their first station
    passengersAtStations.put(passenger, ListOfStations.get(0));
  }

  // Return normally if initial simulation conditions are satisfied, otherwise
  // raises an exception
  public void checkStart() {
    for (Map.Entry<Train, List<Station>> line : lines.entrySet()) {
      Train t = line.getKey();
      List<Station> stations = line.getValue();
      Station start = stations.get(0);
      if (trainsLocations.get(t) != start) { 
        System.out.println(trainsLocations.get(t).toString());
        System.out.println(start.toString());
        throw new RuntimeException("Train Not at Start"); 
      }
    }

    for (Map.Entry<Passenger, List<Station>> journey : journeys.entrySet()) {
      Passenger p = journey.getKey();
      List<Station> stations = journey.getValue();
      Station start = stations.get(0); 
      if (passengersAtStations.get(p) != start) { throw new RuntimeException("Passenger Not at Start"); }
    }
  }

  // Return normally if final simulation conditions are satisfied, otherwise
  // raises an exception
  public void checkEnd() {
    if (passengersOnTrains.size() != 0 || simulate()) {throw new RuntimeException("Passenger Not at End"); }
    for (Map.Entry<Passenger, List<Station>> journey : journeys.entrySet()) {
      Passenger p = journey.getKey();
      if (!arrived(p)) { throw new RuntimeException("Passenger Not at End"); }
    }
  }

  // reset to an empty simulation
  public void reset() {
    lines.clear();
    journeys.clear();
    Pcache.clear();
    Tcache.clear();
    passengersAtStations.clear();
    passengersOnTrains.clear();
    trainsLocations.clear();
    trainsAtStation.clear();
    trainDirection.clear();
  }

  // adds simulation configuration from a file
  public void loadConfig(String filename) {
    try (Reader reader = new FileReader(filename)) {
      Gson gson = new Gson();
      Config config = gson.fromJson(reader, Config.class);

      for (Map.Entry<String, List<String>> line : config.lines.entrySet()) {
        String name = line.getKey();
        List<String> stations = line.getValue();
        addLine(name, stations);
      }

      for (Map.Entry<String, List<String>> line : config.trips.entrySet()) {
        String name = line.getKey();
        List<String> stations = line.getValue();
        addJourney(name, stations);
      }

    } catch (Exception e) {
        e.printStackTrace();
    }
  }
}
