# Elevator System Project

This project is a simulation of an elevator system, designed to manage requests for elevators in a building with multiple floors and elevators.

## Prerequisites

Before running the project, ensure you have the following software installed:

- Java Development Kit (JDK) 17 or later
- Apache Maven 3.6 or later
- An Integrated Development Environment (IDE) like IntelliJ IDEA or Eclipse

## Getting Started

### Clone the Repository

First, clone the repository to your local machine using Git:

```bash
git clone https://github.com/your-username/elevator-system.git
cd elevator-system
```

### Build the Project

Navigate to the project directory and build the project using Maven:

```bash
mvn clean install
```

### Run the Application

After the project is built successfully, you can run the application. You can either run it from your IDE or using the command line.

#### Using Command Line

```bash
mvn spring-boot:run
```

#### Using IDE

1. Open the project in your IDE.
2. Locate the main application class: `ElevatorSystemApplication.java`.
3. Right-click on the class and select "Run".

### Access the Application

Once the application is running, open your web browser and navigate to:

```plaintext
http://localhost:8080
```

### Running Tests

To run the tests, you can use the following Maven command:

```bash
mvn test
```

## Usage

Once the application is running, it simulates an elevator system that can be configured and managed through service methods. The key functionalities include:

- Configuring the number of elevators and floors.
- Requesting an elevator to move from one floor to another.
- Processing pending requests and moving elevators accordingly.

## Project Structure

- `src/main/java/com/kr/elevatorsystem`: Contains the main application code.
  - `ElevatorSystemService.java`: Main service class managing elevator requests and movements.
  - `Elevator.java`: Class representing an individual elevator.
- `src/test/java/com/kr/elevatorsystem`: Contains unit tests for the application.
