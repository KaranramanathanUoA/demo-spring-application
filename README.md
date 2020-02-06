# SpringBoot Demo Application

## Requirements

For building and running the application you will need:

- An IDE (preferably IntelliJ)
- An API testing tool like [Postman](https://www.postman.com/downloads/) 
- [JDK 1.13](https://www.oracle.com/technetwork/java/javase/downloads/jdk13-downloads-5672538.html)
- [Maven 3](https://maven.apache.org)
- [MySQL](https://dev.mysql.com/downloads/mysql/)


## Setting up the environment

- Ensure you have MySQL Server installed. When prompted to choose a password for the user 'root', ensure you set the password as 'root' (without the quotes)
- To setup the database, launch the MySQL workbench and click on the plus sign near 'MySQL Connections' 
- Enter Connection name as DemoDb
- Enter Username as root and leave the other values unchanged.
- Click on Test Connection, you will be prompted to enter the root password.
- Run the following queries in the SQL Query editor
```
CREATE DATABASE DemoApplicationDB;
use DemoApplicationDB;

CREATE TABLE Car (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  color VARCHAR(500) NOT NULL,
  model VARCHAR(5000) NOT NULL
);
```
- Run the following queries in the SQL Query editor (These specific values are required since the integration tests assume that you already have the following records saved in the DB):
```
Insert into Car (id, color, model) values (1, "Blue", "Nissan Mazda");
Insert into Car (id, color, model) values (2, "Black", "Subaru Impreza");
Insert into Car (id, color, model) values (3, "White", "Toyota Corolla")
```

- You will also need to add the database as a data source in your IDE. This can be done by following the steps in [Link](https://stackoverflow.com/a/42498233/8425514)


## Running the Application

- To run the application, navigate to src/main/java/com.example.demoProject.demo/DemoApplication
- Simply click on the Run button, to activate the application.


## Testing the application

- 3 API's are defined in this application - a method to retrive all the car records from the databse, a method to retrieve a car record given a particular id and a method to add new car records to the database
- To test the application works as expected, type http://localhost:8080/cars/ in the browser and you will see the list of car records you have inserted into database displayed in JSON format.
- Similary, you can retrieve a car record from the DB given a vehicleId
- You can also add new Car records to the DB using postman to pass a car record as a body parameter to http://localhost:8080/cars/ . The paramte should be of the form
```
{ "vehicleId": value , "color" : "value", "model" : "value"}
```
- Unit tests and integration tests have also been written for this application.
- To run the unit tests present in the file DemoApplicationWeblayerTests, navigate to src/main/test/java/com.example.demoProject.demo/ DemoApplicationWeblayerTests and simply press the run button for the 2 files present in the directory. 
- To run the integration tests, you will have to ensure that the DemoApplication is running before trigerring these tests as these tests make use of live responses from the server.
