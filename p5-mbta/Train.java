import java.util.*;

public class Train extends Entity {
  private Train(String name) { super(name); }
  
  public static Train make(String name) {
    if (MBTA.Tcache.containsKey(name)) { return MBTA.Tcache.get(name); }
    MBTA.Tcache.put(name, new Train(name));
    return MBTA.Tcache.get(name);
  }
}
