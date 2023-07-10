# ToDo List

This web application is an easy to use **to-do list**.

---

### The main logic of the App:

* displaying the full list of tasks
* adding, editing and deleting tasks
* user authorization

---

### PostgreSQL DB tables:

* tasks
* users

Database tables are scripted with liquibase.

---

### Used technologies:

* Java 17
* Maven 3.1.2
* PostgreSQL 14
* Spring Boot 2.7.6
* Junit Jupiter 5
* AssertJ 3
* Liquibase 4.15.0
* Hibernate
* Thymeleaf 3.0.4
* Bootstrap 5.3.0
* Checkstyle 3.1.2

---



### Run the project

Environment requirements: Java 17.0.2, PostgreSQL 42.5.1, Apache Maven 3.1.2

1. To run the project, you need to clone the project from this repository;
2. Then you need to create a local database "todo_db";
3. Specify the login and password for the database you created in the db/liquibase.properties and src/main/resources/hibernate.cfg.xml file;
4. Run liquibase to pre-create tables;
5. Launch the application using one of the following methods:
   1. Through the Main class, located in the folder src\\main\\java\\ru\\job4j\\todo;
   2. Compiling and running the project via maven with mvn spring-boot:run;
   3. After building the project via maven and running the built file with the command java -jar job4j\_todo-1.0-SNAPSHOT.jar;
6. Open the page [http://localhost:8080/index](http://localhost:8080/index) in the browser

---

### App screenshots


![001.JPG](screenshots/001.JPG)
