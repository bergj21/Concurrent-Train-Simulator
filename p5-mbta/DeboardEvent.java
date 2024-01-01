import java.util.*;

public class DeboardEvent implements Event {
  public final Passenger p; public final Train t; public final Station s;
  public DeboardEvent(Passenger p, Train t, Station s) {
    this.p = p; this.t = t; this.s = s;
  }
  public boolean equals(Object o) {
    if (o instanceof DeboardEvent e) {
      return p.equals(e.p) && t.equals(e.t) && s.equals(e.s);
    }
    return false;
  }
  public int hashCode() {
    return Objects.hash(p, t, s);
  }
  public String toString() {
    return "Passenger " + p + " deboards " + t + " at " + s;
  }
  public List<String> toStringList() {
    return List.of(p.toString(), t.toString(), s.toString());
  }
  public void replayAndCheck(MBTA mbta) {
    if (mbta.passengersOnTrains.get(p) != t) { throw new RuntimeException("Invalid Event: Passenger Not on Train"); }
    if (mbta.trainsLocations.get(t) != s) { throw new RuntimeException("Invalid Event: Train Not at Station"); }
    Station dst = mbta.journeys.get(p).get(0);
    if (s != dst) { throw new RuntimeException("Invalid Event: Station Not Equal to Destination"); }
    mbta.deboard(p, s, t);
  }
}
