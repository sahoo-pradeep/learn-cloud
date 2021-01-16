# learn-cloud

## Services

### 1. book-service

- Default Port: 8501
- For load balancing using Ribbon, create multiple instances with command:
```
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8510 
```

### 2. book-client
- Default Port: 8511

### 3. eureka-server
- Default Port: 8600


## Resources:
 - https://www.javainuse.com/spring/spring_eurekaregister
