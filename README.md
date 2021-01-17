# Learn Spring Cloud

## Services

### 1. book-service (Internal Microservice)

- Default Port: 8501
- To create multiple instances running in different `port`:
```
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8502
```
- Features:
  - netflix-eureka-client

### 2. gateway (Microservice exposed to outside world)
- Default Port: 8511
- Features:
  - netflix-eureka-client
  - netflix-ribbon
  - netflix-hystrix

### 3. eureka-server (Service Registration and Discovery)
- Default Port: 8600
- Eureka: http://localhost:8600/
- Features:
    - netflix-eureka-server

## Resources:
 - https://www.javainuse.com/spring/spring_eurekaregister
