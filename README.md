# learn-cloud

## Services

### 1. book-service

- Default Port: 8501
- For load balancing using Ribbon, create multiple instances with command:
```
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8502
```
- Features:
  - netflix-eureka-client
  - netflix-hystrix

### 2. gateway
- Default Port: 8511
- Features:
  - netflix-eureka-client
  - netflix-ribbon

### 3. eureka-server
- Default Port: 8600
- Eureka: http://localhost:8600/
- Features:
    - netflix-eureka-server

## Resources:
 - https://www.javainuse.com/spring/spring_eurekaregister
