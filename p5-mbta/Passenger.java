import java.util.*;

public class Passenger extends Entity {
  private Passenger(String name) { super(name); }

  public static Passenger make(String name) {
    if (MBTA.Pcache.containsKey(name)) { return MBTA.Pcache.get(name); }
    MBTA.Pcache.put(name, new Passenger(name));
    return MBTA.Pcache.get(name);
  }
}
