# Event Ticketing System

This repository contains a Java-based Command Line Interface event ticketing system designed to manage event tickets, customers, vendors, and related configurations. 
The system leverages JSON for configuration and integrates external libraries to enhance functionality. 
It also contains a web-based application built using Angular for the front end and Spring Boot for the back end, with SQLite as the database.

## Setting up the CLI

### Features
- Manage tickets through a ticket pool.
- Handle customers and vendors with user-related operations.
- Use JSON-based configuration files for system customization.
- Modular design with reusable components.

### Project Structure
```
CLI/
├── src/                     # Source code
│   ├── Configuration.java   # Configuration management
│   ├── Customer.java        # Customer-related operations
│   ├── EventTicketingSystem.java # Main system logic
│   ├── TicketPool.java      # Ticket availability and operations
│   ├── User.java            # User base class
│   └── Vendor.java          # Vendor-related operations
├── lib/                     # Library dependencies
│   ├── error_prone_annotations-2.27.0.jar
│   └── gson-2.11.0.jar
├── config1.json             # JSON configuration file
├── config2.json             # Additional JSON configuration
├── .idea/                   # IntelliJ IDEA project files
├── .gitignore               # Git ignore file
└── out/                     # Compiled Java classes
```

### Prerequisites
- **Java JDK 8 or higher**
- **IntelliJ IDEA** (recommended) or any Java IDE
- **Maven/Gradle** (optional, for managing dependencies)

### Setup Instructions
1. Clone the repository:
   ```bash
   git clone <repository-url>
   ```
2. Open the project in IntelliJ IDEA or your preferred IDE.
3. Add the library dependencies in `lib/` to your classpath.
4. Compile the source code:
   - Using IntelliJ IDEA: Build > Rebuild Project
   - Using CLI:
     ```bash
     javac -cp "lib/*" src/*.java -d out/production/CLI
     ```

### Running the Application
1. Navigate to the compiled classes directory:
   ```bash
   cd out/production/CLI
   ```
2. Execute the main program:
   ```bash
   java EventTicketingSystem
   ```
3. Using IntelliJ IDEA: Run > src/EventTicketingSystem

### Configuration
- I have included 2 Json files that can be loaded to the system by entering the filename.
- Modify `config1.json` and `config2.json` to customize the system's behavior.
  Example:
  ```json
  {
    "maxTicketCapacity": 100,
    "totalTickets": 10,
    "ticketsPerRelease": 4,
    "releaseIntervalMilliseconds": 5000,
    "ticketsPerRetrieval": 2,
    "retrievalIntervalMilliseconds": 2000
  }
  ```
- Or you can manually initialize a config and save it to the current directory using the program and use it again.

## Setting up the Application

### Features

- Manage events, tickets, customers, and vendors efficiently
- User-friendly front-end interface for browsing and purchasing tickets
- RESTful API for seamless client-server communication
- SQLite database for lightweight and efficient data storage
- Modular design with components and APIs

### Tech Stack
#### Frontend (Client)
- **Framework**: Angular
- **Dependencies**: Managed via `npm`
- **Languages**: TypeScript, HTML, CSS

#### Backend (Server)
- **Framework**: Spring Boot
- **Database**: SQLite
- **Build Tool**: Maven
- **Languages**: Java

### Project Structure
#### Client (Angular Frontend)
```
client/
├── node_modules/                                        # Installed dependencies
├── public/                                              # Static assets
├── src/                                                 # Application source code
│   ├── app/
|   │   ├── components/                                  # UI components
|   │   |   ├── configuration-form
|   │   |   ├── control-panel
|   │   |   ├── log-viewer
|   │   |   ├── navbar
|   │   |   ├── past-events
|   │   |   ├── ticket-status
|   │   |   └── timeline-chart
|   │   ├── constant
|   │   |   └── Constant.ts
|   │   ├── model/
|   │   |   ├── class/
|   │   |   |   ├── Configuration.ts
|   │   |   |   ├── ControlPanel.ts
|   │   |   |   └── Event.ts
|   │   |   └── interface/
|   │   |   |   └── api.ts
|   │   ├── services/                                      # API interaction logic & Web Socket Configurations
|   │   |   ├── configuration.service.spec.ts
|   │   |   ├── configuration.service.ts
|   │   |   ├── control-panel.service.spec.ts
|   │   |   ├── control-panel.service.ts
|   │   |   ├── error-handler.service.spec.ts
|   │   |   ├── error-handler.service.ts
|   │   |   ├── event.service.spec.ts
|   │   |   ├── event.service.ts
|   │   |   ├── ticket-status.service.spec.ts
|   │   |   ├── ticket-status.service.ts
|   │   |   ├── web-socket.service.spec.ts
|   │   |   ├── web-socket.service.ts
|   │   ├── app.component.css
|   │   ├── app.component.html
|   │   ├── app.component.spec.ts
|   │   ├── app.component.ts
|   │   ├── app.config.ts
|   │   └── app.routes.ts
│   ├── environments/
|   │   ├── environment.development.ts
|   │   └── environment.ts
├── angular.json                                          # Angular CLI configuration
├── package.json                                          # Node.js dependencies
└── tsconfig.json                                         # TypeScript configuration

```

#### Server (Spring Boot Backend)
```
Server/
├── pom.xml                                               # Maven project configuration
├── src/                                                  # Application source code
│   └── main/
|   │   └── java/
|   │   |   └── com.example.server/
|   │   |   |   ├── configs                               # WebSocket & Scheduler configs
|   │   |   |   ├── controller                            # Controller classes
|   │   |   |   ├── dto                                   # Data transfer objects
|   │   |   |   ├── entity                                # Entity classes
|   │   |   |   ├── exception                             # Exception handling classes
|   │   |   |   ├── logic                                 # Logic layer
|   │   |   |   ├── repo                                  # Repository layer that connects to the database
|   │   |   |   └── service                               # Service layer 
├── Server.db                                             # SQLite database file
├── target/                                               # Compiled backend artifacts
└── HELP.md                                               # Spring Boot autogenerated help
```

### Prerequisites
- **Frontend**: Node.js and npm installed.
- **Backend**: Java 17 or later, Maven installed.
- **SQLite3**: SQLite3 installed**

### Setup
- Clone the repository:
   ```bash
   git clone <repository-url>
   ```

#### Frontend
1. Navigate to the `client` folder:
   ```bash
   cd client
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Run the development server:
   ```bash
   ng serve
   ```
4. Open [http://localhost:4200](http://localhost:4200) in your browser.
5. **Using VS Code**:
    - Open the `client` folder in VS Code.
    - Install the recommended extensions (e.g., Angular Language Service).
    - Use the integrated terminal to run the above commands.

#### Backend
1. Navigate to the `Server` folder:
   ```bash
   cd Server
   ```
2. Run the application using Maven:
   ```bash
   ./mvnw spring-boot:run
   ```
3. The backend will be available at [http://localhost:8080](http://localhost:8080).
4. **Using IntelliJ IDEA**:
    - Open the `Server` folder in IntelliJ IDEA as a project.
    - Ensure that Maven and JDK are configured correctly.
    - Run the `Application` class (usually located in `src/main/java`).

#### Database
The system uses an SQLite database (`Server.db`) located in the backend directory. No additional setup is required.

### License
This project is licensed under the MIT License. See `LICENSE` for details.

### Contact
For any inquiries or issues, please open an issue in the repository or contact,
- Email: [venukdesoyza@email.com](mailto:venukdesoyza@email.com)
- LinkedIn: [@Venuk De Soyza](https://www.linkedin.com/in/venuk-de-soyza)
- GitHub: [@VenukSyz](https://github.com/VenukSyz)
