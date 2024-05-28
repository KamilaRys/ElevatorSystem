package com.kr.elevatorsystem;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ElevatorSystemService {
    private final List<Elevator> elevators;
    private final List<Request> pendingRequests;
    private int numberOfFloors;

    public ElevatorSystemService() {
        elevators = new ArrayList<>();
        pendingRequests = new ArrayList<>();
    }

    public void configure(int numberOfElevators, int numberOfFloors) {
        this.numberOfFloors = numberOfFloors;
        elevators.clear();
        for (int i = 0; i < numberOfElevators; i++) {
            elevators.add(new Elevator(i));
        }
    }

    public List<Request> getPendingRequests() {
        return pendingRequests;
    }

    public void step() {
        for (Elevator elevator : elevators) {
            elevator.move();
        }
        processPendingRequests();
    }

    public List<Elevator> getElevators() {
        return elevators;
    }

    public void requestElevator(int startFloor, int targetFloor) {
        for (Request request : pendingRequests) {
            if (request.startFloor == startFloor && request.targetFloor == targetFloor) {
                return;
            }
        }

        for (Elevator elevator : elevators) {
            if (elevator.getTargetFloorsQueue().contains(startFloor) && elevator.getTargetFloorsQueue().contains(targetFloor)) {
                return;
            }
        }

        Elevator elevator = findBestElevator(startFloor, targetFloor);
        if (elevator != null) {
            elevator.addTargetFloor(startFloor, targetFloor);
        } else {
            pendingRequests.add(new Request(startFloor, targetFloor));
        }
    }


    private Elevator findBestElevator(int startFloor, int targetFloor) {
        Elevator bestElevator = null;
        int minDistance = Integer.MAX_VALUE;

        for (Elevator elevator : elevators) {
            if (elevator.isIdle()) {
                int distance = Math.abs(elevator.getCurrentFloor() - startFloor);
                if (distance < minDistance) {
                    minDistance = distance;
                    bestElevator = elevator;
                }
            }
        }

        if (bestElevator == null) {
            for (Elevator elevator : elevators) {
                if ((elevator.getDirection() == Elevator.Direction.UP && elevator.getCurrentFloor() <= startFloor && targetFloor > startFloor) ||
                        (elevator.getDirection() == Elevator.Direction.DOWN && elevator.getCurrentFloor() >= startFloor && targetFloor < startFloor) ||
                        (elevator.getCurrentFloor() == startFloor && isFloorOnTheWay(elevator, targetFloor))) {
                    int distance = calculateDistance(elevator, startFloor, targetFloor);
                    if (distance < minDistance) {
                        minDistance = distance;
                        bestElevator = elevator;
                    }
                }
            }
        }

        return bestElevator;
    }

    private int calculateDistance(Elevator elevator, int startFloor, int targetFloor) {
        int distance = Math.abs(elevator.getCurrentFloor() - startFloor);
        if (elevator.getDirection() == Elevator.Direction.UP && targetFloor > startFloor) {
            distance += Math.abs(targetFloor - startFloor);
        } else if (elevator.getDirection() == Elevator.Direction.DOWN && targetFloor < startFloor) {
            distance += Math.abs(targetFloor - startFloor);
        } else {
            distance += numberOfFloors;
        }
        return distance;
    }

    private boolean isFloorOnTheWay(Elevator elevator, int targetFloor) {
        if (elevator.getDirection() == Elevator.Direction.UP) {
            return targetFloor > elevator.getCurrentFloor() && targetFloor <= numberOfFloors;
        } else if (elevator.getDirection() == Elevator.Direction.DOWN) {
            return targetFloor < elevator.getCurrentFloor() && targetFloor >= 0;
        } else {
            return false;
        }
    }

    private void processPendingRequests() {
        List<Request> toRemove = new ArrayList<>();
        for (Request request : pendingRequests) {
            Elevator elevator = findBestElevator(request.startFloor(), request.targetFloor());
            if (elevator != null) {
                elevator.addTargetFloor(request.startFloor(), request.targetFloor());
                toRemove.add(request);
            }
        }
        pendingRequests.removeAll(toRemove);
    }


    public record Request(int startFloor, int targetFloor) {
    }
}
