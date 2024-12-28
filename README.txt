This project is a CRUD (Create, Read, Update, Delete) API application built with Spring Boot. 
It uses PostgreSQL as the database, runs PostgreSQL in Docker, and secures API endpoints using JWT authentication.

## Prerequisite
Before setting up the application, ensure you have the following tools installed:
Java JDK 17 - or later
Gradle - for building the project
Postman - for testing API endpoints

## Setup and Running the Application

### 1. Clone the Repository
1. git clone https://github.com/WetHorse/SpringBootTask.git
. I use GitHub Desktop (lammer moment)

### 2. Download missing files (at IDE's discretion)

## 3. Docker ( Docker technology is not implemented, because there was no experience working with it )

### 4. Connection to PostgresDB 
1. Go into - resources/application.properties
2. Input your connection data

//
spring.datasource.url=jdbc:postgresql://localhost:<your_port>/<your_database_name>
spring.datasource.username=<datasorce_username>   --(<postgres> default connection name)
spring.datasource.password=<datasource_password>  --(if needed)
spring.jpa.hibernate.ddl-auto=create-drop 
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
//

### 5. Test endpoints using Postman

--Register user--

Method: POST
URL: http://localhost:8080/api/users/register
Content-Type: application/json
Body:
{
  "username": "user1",
  "password": "pass"
}

Response 200: (Creates new user in repository)
"Registration complete!"

--Login user--

Method: POST
URL: http://localhost:8080/api/users/login
Content-Type: application/json
Body:
{
  "username": "user1",    //user data has to be the same
  "password": "admin123"
}

Response 200: (Creates token for login session)
Body:
{
  "accessToken": "jwt-token",
  "tokenType": "Bearer "
}

--Get all users (logged users only)--

Method: GET
URL: http://localhost:8080/api/users/users
Content-Type: application/json
Headers: Authorization | Bearer <your-jwt-token>

Response 200: (Returns list of users json)
Body:
{
  "id": 1
  "username": "user1",    //user data has to be the same
  "password": "123"
}
... and so on (number of exist users)


--Delete user by id (logged users only)--

Method: DELETE
URL: http://localhost:8080/api/users/{user_id}
Content-Type: application/json
Headers: Authorization | Bearer <your-jwt-token>

Response 200: (User with id {user_id} deleted) 



