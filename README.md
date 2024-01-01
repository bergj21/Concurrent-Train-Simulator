*******************************************************************************
*       Project: Concurrent Train Simulator                                   *
*       Authors: Jack Berg                                                    *
*******************************************************************************

In this project, I implemented a multi-threaded simulation of the MBTA. In the simulation, passengers will ride trains between stations, boarding and deboarding (getting on and off) to complete their journey. The simulation is multi-threaded, with a thread for each passenger and each train. In addition, the simulation generates a log showing the movements of passengers and trains. To make testing and debugging easier, the program includes a verifier that checks that the simulation result is sensible, e.g., passengers can only deboard trains at the stations the trains are at, trains must move along their lines in sequence, etc.
