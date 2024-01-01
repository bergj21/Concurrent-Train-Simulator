import java.io.*;
import java.util.*;

import java.util.concurrent.locks.*;

public class TrainThread extends Thread {
    private Train train;
    private MBTA mbta;
    private Log log;

    private Lock l = new ReentrantLock();
    private Condition c = l.newCondition();

    public TrainThread(Train train, MBTA mbta, Log log) {
        this.train = train;
        this.mbta = mbta;
        this.log = log;
    }

    public void run() {
        while (mbta.simulate()) {

            Station currentStation = mbta.trainsLocations.get(train);
            Station nextStation = mbta.nextStation(train); 
            try {
                synchronized(currentStation) {

                    synchronized(nextStation) {
                        while (mbta.trainsAtStation.get(nextStation) != null) {
                            if (!mbta.simulate()) {
                                return;
                            }
                            nextStation.wait();
                        }
                        mbta.moveTrain(train, currentStation, nextStation);
                        log.train_moves(train, currentStation, nextStation);
                        // synchronized(currentStation) {
                        //     currentStation.notifyAll();
                        // }
                        nextStation.notifyAll();
                    }
                    currentStation.notifyAll();
                }
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }   
        }
        // System.out.println(train.toString() + " Thread Complete");
    }
}