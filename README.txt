1. Service exposes Rest API to accept money transfers to different accounts. It also allows to create new accounts and get information about them
2. Tech stack used: Kotlin, Gradle, Springboot, h2 for storing test data
3. Api documented using swagger, it is available on http://localhost:8080/swagger-ui/index.html after start of application
4. Application logs all incoming HTTP requests via LogFilter
5. Test coverage shown by IDEA is 93%, for testing were used JUnit and MockMvc