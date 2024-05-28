package com.kr.elevatorsystem;

import java.util.LinkedList;
import java.util.Queue;

public class Elevator {
    private final int id;
    private final Queue<Integer> targetFloors;
    private final Integer finalDestination;
    private int currentFloor;
    private Direction direction;

    public enum Direction {
        UP, DOWN, IDLE
    }

    public Elevator(int id) {
        this.id = id;
        this.currentFloor = 0;
        this.targetFloors = new LinkedList<>();
        this.finalDestination = null;
        this.direction = Direction.IDLE;
    }

    public int getId() {
        return id;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public Queue<Integer> getTargetFloorsQueue() {
        return targetFloors;
    }

    public String getTargetFloors() {
        return targetFloors.toString();
    }

    public Direction getDirection() {
        return direction;
    }

    public void addTargetFloor(int startFloor, int targetFloor) {
        if (!targetFloors.contains(startFloor)) {
            targetFloors.add(startFloor);
        }
        if (!targetFloors.contains(targetFloor)) {
            targetFloors.add(targetFloor);
        }
        updateDirection();
    }


    private void updateDirection() {
        if (!targetFloors.isEmpty()) {
            if (currentFloor < targetFloors.peek()) {
                direction = Direction.UP;
            } else if (currentFloor > targetFloors.peek()) {
                direction = Direction.DOWN;
            }
        } else {
            direction = Direction.IDLE;
        }
    }

    public void move() {
        if (!targetFloors.isEmpty()) {
            int targetFloor = targetFloors.peek();
            if (currentFloor < targetFloor) {
                currentFloor++;
            } else if (currentFloor > targetFloor) {
                currentFloor--;
            } else {
                targetFloors.poll();
                updateDirection();
            }
        } else {
            direction = Direction.IDLE;
        }
    }

    public boolean isIdle() {
        return targetFloors.isEmpty() && finalDestination == null && direction == Direction.IDLE;
    }
}
