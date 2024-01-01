import java.io.*;
import java.util.*;

import java.util.concurrent.locks.*;

public class PassengerThread extends Thread {
    private Passenger passenger;
    private MBTA mbta;
    private Log log;

    // private Lock l = new ReentrantLock();
    // private Condition c = l.newCondition();

    public PassengerThread(Passenger passenger, MBTA mbta, Log log) {
        this.passenger = passenger;
        this.mbta = mbta;
        this.log = log;
    }

    public void run() {
        while (!mbta.arrived(passenger)) {
            Station currentStation = mbta.passengersAtStations.get(passenger);
            // Passenger is waiting at a station
            if (currentStation != null) {
                try {
                   Train train = mbta.getTrain(passenger);
                    synchronized(currentStation) {
                        while (mbta.trainsAtStation.get(currentStation) != train) {
                            currentStation.wait();
                        } 
                        mbta.board(passenger, currentStation, train);
                        log.passenger_boards(passenger, train, currentStation);
                        currentStation.notifyAll();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
            }
            // Passenger is on a train
            else {
                // Train that the passenger is on
                Train train = mbta.passengersOnTrains.get(passenger);
                // The destination station of the passenger
                Station destination = mbta.destination(passenger);
                try {
                   synchronized(destination) {
                        while (mbta.trainsLocations.get(train) != destination) {
                            destination.wait();
                        }
                        mbta.deboard(passenger, destination, train);
                        log.passenger_deboards(passenger, train, destination);
                        destination.notifyAll();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        // System.out.println("Passenger Thread Complete");
    }
}