# SmartHRM

SmartHRM is a microservice-based human resource management system built with Spring Boot and Spring Cloud, designed to help enterprises efficiently manage employee, skill, project, and training information.

## Multi-Language Index
- [README.zh-CN.md](docs/README.zh-CN.md) - Chinese
- [README.ja.md](docs/README.ja.md) - Japanese
- README-en.md - Here

## Features Overview

- **Employee Management**: Supports CRUD operations for employees.
- **Skill Matching**: Intelligent matching of suitable employees based on project skill requirements.
- **Project Matching**: Fuzzy search project by name or involved employee's id.
- **Task Assignment**: Assign tasks to project members and track completion rates.
- **Department and Project Management**: Manage organizational structure and project information.
- **Training Management**: Manage employee training records.

## Technology Stack

### Backend
- **Spring Boot 3.5.7** - Application framework
- **Spring Cloud 2025.0.1** - Microservice framework
- **Spring Cloud Alibaba 2025.0.0.0** - Alibaba Cloud integration
- **Spring Cloud Gateway** - API Gateway
- **Nacos** - Service discovery and configuration center
- **MongoDB** - Database
- **Lombok** - Code simplification

### Frontend
- **Vue 3** - Progressive JavaScript framework
- **Vite** - Next-generation frontend build tool
- **Element Plus** - Vue 3 component library
- **Vue Router** - Official routing manager
- **Axios** - HTTP client

## Microservice Architecture

SmartHRM adopts a microservice architecture with the following services:

### Service List

| Service Name | Port | Description |
|-------------|------|-------------|
| GatewayService | 8090 | API Gateway for routing requests |
| DepartmentService | 8081 | Department management service |
| EmployeeService | 8082 | Employee management service |
| ProjectService | 8083 | Project and task management service |
| SkillService | 8084 | Skill and skill matching service |
| TrainingService | 8085 | Training management service |
| Nacos | 8848 | Service registry and configuration center |

### Architecture Overview

The system uses Spring Cloud Gateway as the unified entry point, which routes requests to different microservices based on URL paths. All services register with Nacos for service discovery. The frontend Vue application communicates with backend services through the Gateway.

## Module Description

### Core Services

- **DepartmentService**: Manages departmental information, including department CRUD operations and employee transfers.
- **EmployeeService**: Manages employee information, including basic CRUD operations.
- **ProjectService**: Manages project & task information, with basic CRUD operations for project addition, task assignment, and project progress tracking.
- **SkillService**: Manages skill information and provides skill matching functionality.
- **SkillMatchController**: Provides skill matching functionality, allowing search for suitable employees based on required project skills.
- **TrainingService**: Manages training information, including adding training sessions.
- **GatewayService**: API Gateway that routes requests to appropriate microservices.

### Commons Module

- **Entity Classes**: Core data models including Employee, Skill, Project, Department, Task, and Training.
- **Dao**: Direct access to MongoDB operations and various queries (fuzzy, pagination).
- **Repo**: Entity class data access layer.
- **DTO**: Simple data transfer objects to reduce redundant data transmission, containing data transfer methods and special processing methods.

### Entity Classes

- **Employee**: Employee entity containing name, department, skill list, and other information.
- **Skill**: Skill entity containing skill name, type, and other information.
- **Project**: Project entity containing project name, member list, required skills, and other information.
- **Department**: Department entity containing department name, manager ID, employee list, and other information.
- **Task**: Tasks assigned to multiple employees within a project.
- **Training**: Training courses designated for multiple employees.

## Quick Start

### Prerequisites

- Java 17
- MongoDB
- Maven
- Node.js (for frontend)
- Nacos Server

### Installation & Setup

#### 1. Install and Start Nacos

Download Nacos from [Nacos GitHub](https://github.com/alibaba/nacos/releases) and start it:

```bash
# Windows
startup.cmd

# Linux/Mac
sh startup.sh -m standalone
```

Access Nacos console at: http://localhost:8848/nacos
Note: Please modify the default credentials in production environment for security.

#### 2. Configure Database

Start MongoDB service:

```bash
# Windows
net start MongoDB

# Linux/Mac
mongod --dbpath /path/to/data
```

Configure MongoDB connection in each service's `application.yml`:
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/smartHRM
```

#### 3. Configure Nacos Address

Each microservice's `application.yml` should have:
```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
```

### Run the Project

#### Start Backend Services

**Option 1: Using IDE**
1. Open the project in IntelliJ IDEA or Eclipse
2. Run each service's main application class in order:
   - DepartmentApplication.java
   - EmployeeApplication.java
   - ProjectApplication.java
   - SkillApplication.java
   - TrainingApplication.java
   - gatewayApplication.java (Gateway)

**Option 2: Using Maven**
```bash
# Build all modules
mvn clean package

# Run each service (from respective module directory)
java -jar target/DepartmentService.jar
java -jar target/EmployeeService.jar
java -jar target/ProjectService.jar
java -jar target/SkillService.jar
java -jar target/TrainingService.jar
java -jar target/GatewayService.jar
```

#### Start Frontend

```bash
cd frontend
npm install
npm run dev
```

### Access the System

- **Frontend Application**: http://localhost:3000
- **API Gateway**: http://localhost:8090
- **Nacos Console**: http://localhost:8848/nacos

## Usage Instructions

### Frontend Routes

- **Employee Management**: Visit `/employees` route to perform add, modify, delete operations on employees.
- **Department Management**: Access `/departments` route to add, modify, delete departments.
- **Skill Management**: Access `/skills` route to manage skill information.
- **Training Management**: Visit `/trainings` route to manage training information.
- **Skill Matching**: Access `/skillmatch` route, select required skills, and the system will automatically match eligible employees.
- **Project Matching Management**: Access `/projectmatch` route to fuzzy search project details by project name or involved employee id.

### Backend API Routes (via Gateway)

All backend APIs are accessed through the Gateway at http://localhost:8090:

- `/employees/**` - Employee Service
- `/departments/**` - Department Service
- `/projects/**` - Project Service
- `/skill/**` - Skill Service
- `/training/**` - Training Service
- `/skillmatch/**` - Skill Matching Service
- `/projectmatch/**` - Project Matching Service

## Configuration

### Service Configuration

Each microservice has its own `application.yml` file located in `src/main/resources/`:

- `GatewayService/src/main/resources/application.yml` - Gateway routing configuration
- `EmployeeService/src/main/resources/application.yml` - Employee service configuration
- `DepartmentService/src/main/resources/application.yml` - Department service configuration
- `ProjectService/src/main/resources/application.yml` - Project service configuration
- `SkillService/src/main/resources/application.yml` - Skill service configuration
- `TrainingService/src/main/resources/application.yml` - Training service configuration

### Frontend Configuration

Frontend API proxy is configured in `frontend/vite.config.js`:

```javascript
server: {
  proxy: {
    '/api': {
      target: 'http://localhost:8090',
      changeOrigin: true,
      rewrite: (path) => path.replace(/^\/api/, '')
    }
  }
}
```

## Contribution Guidelines

Contributions and suggestions are welcome! Please follow these steps:

1. Fork this repository.
2. Create a new branch (`git checkout -b feature/new-feature`).
3. After fetching the latest update from the remote repository, add your changes to git version control.
4. Commit your changes with a description (`git commit -m 'Add new feature'`).
5. Push to the remote branch (`git push origin feature/new-feature`).
6. Submit a Pull Request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Demo Pages

### **Employee Management**: `/employees/`

#### 	Total Interface

[Employee Management]()

#### 	Add Employee

[Add Employee]()

### **Department Management**: `/departments/`

#### 	Total Interface

[Department Management]()

#### 	Add Department

[Add Department]()

### **Skill Matching**: `/skillmatch/`

[Skill Matching]()

### **Project Management**: `/projectmatch/`

#### 	Total Interface

[Project Management]()

#### 	Assign Tasks

[Task Assignment]()

### 	**Skill & Training Management**: `/training/`

[Skill & Training Management]()

## Additional Resources

- [QUICK_START.md](QUICK_START.md) - Detailed quick start guide
- [frontend/README.md](frontend/README.md) - Frontend documentation
- [docs/README.zh-CN.md](docs/README.zh-CN.md) - Chinese documentation
