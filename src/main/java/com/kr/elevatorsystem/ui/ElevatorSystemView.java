package com.kr.elevatorsystem.ui;

import com.kr.elevatorsystem.Elevator;
import com.kr.elevatorsystem.ElevatorSystemService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
public class ElevatorSystemView extends VerticalLayout {

    private final ElevatorSystemService elevatorSystem;
    private final VerticalLayout mainLayout;
    private Grid<Elevator> elevatorGrid;

    @Autowired
    public ElevatorSystemView(ElevatorSystemService elevatorSystem) {
        this.elevatorSystem = elevatorSystem;
        this.mainLayout = new VerticalLayout();
        add(mainLayout);
    }

    @PostConstruct
    private void initializeView() {
        showConfigurationForm();
    }


    private void showConfigurationForm() {
        NumberField numberOfElevatorsField = new NumberField("Number of Elevators");
        NumberField numberOfFloorsField = new NumberField("Number of Floors");

        Button configureButton = new Button("Configure", event -> {
            int numberOfElevators = numberOfElevatorsField.getValue().intValue();
            int numberOfFloors = numberOfFloorsField.getValue().intValue();
            elevatorSystem.configure(numberOfElevators, numberOfFloors);
            showElevatorManagementView();
        });

        mainLayout.removeAll();
        mainLayout.add(numberOfElevatorsField, numberOfFloorsField, configureButton);
    }

    private void showElevatorManagementView() {
        NumberField startFloorField = new NumberField("Start Floor");
        NumberField targetFloorField = new NumberField("Target Floor");
        Button requestButton = new Button("Request Elevator", e -> {
            int startFloor = startFloorField.getValue().intValue();
            int targetFloor = targetFloorField.getValue().intValue();
            elevatorSystem.requestElevator(startFloor, targetFloor);
            updateGrid();
        });

        elevatorGrid = new Grid<>(Elevator.class);
        elevatorGrid.addColumn(Elevator::getId).setHeader("Elevator ID");
        elevatorGrid.addColumn(Elevator::getCurrentFloor).setHeader("Current Floor");
        elevatorGrid.addColumn(Elevator::getTargetFloors).setHeader("Target Floors");

        elevatorGrid.setItems(elevatorSystem.getElevators());

        Button stepButton = new Button("Next Step", e -> {
            elevatorSystem.step();
            updateGrid();
        });

        mainLayout.removeAll();
        mainLayout.add(startFloorField, targetFloorField, requestButton, elevatorGrid, stepButton);
    }

    private void updateGrid() {
        elevatorGrid.getDataProvider().refreshAll();
    }
}