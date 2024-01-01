import java.io.*;
import java.util.*;

public class Sim {

  public static void run_sim(MBTA mbta, Log log) {
    
    List<Thread> threads = new ArrayList<>();

    for (Passenger p : mbta.journeys.keySet()) {
      threads.add(new PassengerThread(p, mbta, log));
    }

    for (Train t : mbta.lines.keySet()) {
      threads.add(new TrainThread(t, mbta, log));
    }

    for (Thread thread : threads) {
      thread.start();
    }

    try {
      for (Thread thread : threads) {
        thread.join();
      } 
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public static void main(String[] args) throws Exception {
    if (args.length != 1) {
      System.out.println("usage: ./sim <config file>");
      System.exit(1);
    }

    MBTA mbta = new MBTA();
    mbta.loadConfig(args[0]);

    Log log = new Log();

    run_sim(mbta, log);

    String s = new LogJson(log).toJson();
    PrintWriter out = new PrintWriter("log.json");
    out.print(s);
    out.close();

    mbta.reset();
    mbta.loadConfig(args[0]);
    Verify.verify(mbta, log);
  }
}
