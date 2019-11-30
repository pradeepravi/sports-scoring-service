# sports-scoring-service

### Spring Boot REST API Service
#### mvn spring-boot:run

### To start the application: 
1) I have used PostGreSql as DB
2) Run queries in db_scripts.sql under resources before you start the server
3) Following application.properties are important to configure the API

spring.datasource.url=jdbc:postgresql://localhost:5432/sports-score
spring.datasource.username=postgres
spring.datasource.password=admin

### Swagger API
http://localhost:8080/swagger-ui.html

### URL of the Angular Application tat shouldn't be blocked by the Services
supported.settings.cors_origin=http://localhost:4200
