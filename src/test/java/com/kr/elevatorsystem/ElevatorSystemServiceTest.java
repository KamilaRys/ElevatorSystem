package com.kr.elevatorsystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorSystemServiceTest {

    private ElevatorSystemService elevatorSystemService;

    @BeforeEach
    void setUp() {
        elevatorSystemService = new ElevatorSystemService();
        elevatorSystemService.configure(3, 10);
    }

    @Test
    void testRequestElevator_IdleElevatorsAvailable() {
        // given
        int startFloor = 3;
        int targetFloor = 7;

        // when
        elevatorSystemService.requestElevator(startFloor, targetFloor);

        // then
        Elevator assignedElevator = elevatorSystemService.getElevators().stream()
                .filter(elevator -> !elevator.getTargetFloorsQueue().isEmpty() &&
                        elevator.getTargetFloorsQueue().contains(startFloor))
                .findFirst()
                .orElse(null);

        assertNotNull(assignedElevator, "No elevator was assigned the request.");
        assertEquals(startFloor, assignedElevator.getTargetFloorsQueue().peek(), "The start floor is not the first target in the queue.");
        assertTrue(assignedElevator.getTargetFloorsQueue().contains(targetFloor), "The target floor is not in the target queue.");
    }

    @Test
    void testRequestElevator_BusyElevatorsAvailable() {
        // given
        elevatorSystemService.requestElevator(1, 5); // Assume elevator 0 handles this
        elevatorSystemService.requestElevator(2, 6); // Assume elevator 1 handles this
        elevatorSystemService.step();

        // when
        elevatorSystemService.requestElevator(3, 7);

        // then
        boolean requestHandled = elevatorSystemService.getElevators().stream()
                .anyMatch(elevator -> elevator.getTargetFloorsQueue().contains(3) && elevator.getTargetFloorsQueue().contains(7));
        assertTrue(requestHandled);
    }

    @Test
    void testRequestElevator_BusyElevatorsNotOnTheWay() {
        // given
        elevatorSystemService.requestElevator(1, 5); // Assume elevator 0 handles this
        elevatorSystemService.requestElevator(2, 6); // Assume elevator 1 handles this
        elevatorSystemService.requestElevator(0, 3); // Assume elevator 2 handles this
        elevatorSystemService.step();
        elevatorSystemService.step();

        // when
        elevatorSystemService.requestElevator(0, 1);

        // then
        assertEquals(1, elevatorSystemService.getPendingRequests().size());
    }

    @Test
    void testStep_ProcessPendingRequests() {
        // given
        elevatorSystemService.requestElevator(1, 5);
        elevatorSystemService.requestElevator(2, 6);
        elevatorSystemService.requestElevator(3, 7);
        elevatorSystemService.step();

        // when
        elevatorSystemService.requestElevator(1, 5);
        elevatorSystemService.requestElevator(2, 6);
        elevatorSystemService.requestElevator(3, 7);
        elevatorSystemService.step();

        // then
        assertTrue(elevatorSystemService.getPendingRequests().isEmpty());
    }
}
