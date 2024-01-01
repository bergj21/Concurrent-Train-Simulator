import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;

public class Tests {
  @Test 
  public void testPass() {
    assertTrue("true should be true", true);
  }

  @Test 
  public void verify1() {
    MBTA mbta = new MBTA();
    mbta.addLine("Purple", List.of("Hudons Yds", "Port Authority", "Times Sq"));
    mbta.addJourney("Lebron", List.of("Hudons Yds", "Times Sq"));
    Train train = Train.make("Purple");
    Station st1 = Station.make("Hudons Yds");
    Station st2 = Station.make("Port Authority");
    Station st3 = Station.make("Times Sq");
    Passenger p = Passenger.make("Lebron");
    Log log = new Log(List.of(new BoardEvent(p, train, st1),
                              new MoveEvent(train, st1, st2),
                              new MoveEvent(train, st2, st3),
                              new DeboardEvent(p, train, st3)));
    Verify.verify(mbta, log);
  }

  // @Test 
  public void verify3() {
    MBTA mbta = new MBTA();
    mbta.addLine("Purple", List.of("Hudons Yds", "Port Authority", "Times Sq"));
    mbta.addJourney("Lebron", List.of("Hudons Yds", "Times Sq"));
    Train train = Train.make("Purple");
    Station st1 = Station.make("Hudons Yds");
    Station st2 = Station.make("Port Authority");
    Station st3 = Station.make("Times Sq");
    Passenger p = Passenger.make("Lebron");
    Log log = new Log(List.of(new BoardEvent(p, train, st1),
                              new MoveEvent(train, st1, st2),
                              new MoveEvent(train, st2, st3)));
    Verify.verify(mbta, log);
  }

  // @Test 
  public void verify2() {
    MBTA mbta = new MBTA();
    mbta.addLine("Purple", List.of("Hudons Yds", "Port Authority", "Times Sq"));
    mbta.addJourney("Lebron", List.of("Hudons Yds", "Hudons Yds"));
    Train train = Train.make("Purple");
    Station st1 = Station.make("Hudons Yds");
    Passenger p = Passenger.make("Lebron");
    // Log log = new Log(List.of(new BoardEvent(p, train, st1), new DeboardEvent(p, train, st1)));
    Log log = new Log();
    Sim.run_sim(mbta, log);
    mbta.reset();
    mbta.addLine("Purple", List.of("Hudons Yds", "Port Authority", "Times Sq"));
    mbta.addJourney("Lebron", List.of("Hudons Yds", "Hudons Yds"));
    Verify.verify(mbta, log);
  }

  // @Test 
  public void class_test_verify_trip_complete() {
    MBTA new_mbta = new MBTA();
    Station oakgrove = Station.make("Oak Grove");
    Station maldencenter = Station.make("Malden Center");
    Station wellington = Station.make("Wellington");
    Station assembly = Station.make("Assembly");
    Passenger p = Passenger.make("Terve");
    Train orange = Train.make("orange");

    new_mbta.addLine("orange", List.of("Oak Grove", "Malden Center", "Wellington", "Assembly"));
    new_mbta.addJourney("Terve", List.of("Oak Grove", "Wellington", "Malden Center"));

    // Log events = new Log(List.of(
    //     new BoardEvent(p, orange, oakgrove),
    //     new MoveEvent(orange, oakgrove, maldencenter),
    //     new MoveEvent(orange, maldencenter, wellington),
    //     new DeboardEvent(p, orange, wellington),
    //     new MoveEvent(orange, wellington, assembly),
    //     new MoveEvent(orange, assembly, wellington),
    //     new BoardEvent(p, orange, wellington),
    //     new MoveEvent(orange, wellington, maldencenter),
    //     new DeboardEvent(p, orange, maldencenter)));
    Log log = new Log();
    Sim.run_sim(new_mbta, log);
    new_mbta.reset();
    new_mbta.addLine("orange", List.of("Oak Grove", "Malden Center", "Wellington", "Assembly"));
    new_mbta.addJourney("Terve", List.of("Oak Grove", "Wellington", "Malden Center"));
    Verify.verify(new_mbta, log);
  }

  // @Test 
  public void worldTour() {
      MBTA mbta = new MBTA();
      Train t1 = Train.make("Euro");
      Train t2 = Train.make("World");

      Station s1 = Station.make("France");
      Station s2 = Station.make("England");
      Station s3 = Station.make("Germany");
      Station s4 = Station.make("Brazil");

      Passenger p1 = Passenger.make("Messi");
      mbta.addLine("Euro", List.of("France", "England"));
      mbta.addLine("World", List.of("Germany", "England", "Brazil"));
      mbta.addJourney("Messi", List.of("France", "England", "Brazil"));
      // Log log = new Log(List.of(
      //                           new BoardEvent(p1, t1, s1),
      //                           new MoveEvent(t1, s1, s2),
      //                           new DeboardEvent(p1, t1, s2),
      //                           new MoveEvent(t1, s2, s1),

      //                           new MoveEvent(t2, s3, s2),
                                
      //                           new BoardEvent(p1, t2, s2),

      //                           new MoveEvent(t2, s2, s4),
      //                           new DeboardEvent(p1, t2, s4)
      //                           ));
      Log log = new Log();
      Sim.run_sim(mbta, log);
      mbta.reset();
      mbta.addLine("Euro", List.of("France", "England"));
      mbta.addLine("World", List.of("Germany", "England", "Brazil"));
      mbta.addJourney("Messi", List.of("France", "England", "Brazil"));
      Verify.verify(mbta, log);
  }
}
