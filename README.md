# Learn Spring Cloud

## Services

### 1. book-service (Internal Microservice)

- Port: 8501
- To create multiple instances running in different `port`:
```
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8502
```
- Features:
  - netflix-eureka-client

### 2. gateway (Microservice exposed to outside world)
- Port: 8511
- Features:
  - netflix-eureka-client
  - netflix-ribbon
  - netflix-hystrix
  - openfeign

### 3. eureka-server (Service Registration and Discovery)
- Port: 8761
- Eureka: http://localhost:8761/
- Features:
  - netflix-eureka-server
  
### 4. zuul-service (Gateway to internal services)
- Port: 8611
- Features:
  - netflix-eureka-client
  - netflix-zuul

## Resources:
 - https://www.javainuse.com/spring/spring_eurekaregister
