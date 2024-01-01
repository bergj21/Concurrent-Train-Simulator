import java.util.*;

public class Station extends Entity {
  private Station(String name) { super(name); }

  private static HashMap<String, Station> cache = new HashMap<>();

  public static Station make(String name) {
    if (cache.containsKey(name)) { return cache.get(name); }
    cache.put(name, new Station(name));
    return cache.get(name);
  }
}
