package com.example.gateway.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ClientBookController {

    private final DiscoveryClient discoveryClient;
    private final LoadBalancerClient loadBalancerClient;
    private final HttpBookService httpBookService;

    @Autowired
    public ClientBookController(DiscoveryClient discoveryClient, LoadBalancerClient loadBalancerClient, HttpBookService httpBookService) {
        this.discoveryClient = discoveryClient;
        this.loadBalancerClient = loadBalancerClient;
        this.httpBookService = httpBookService;
    }

    @Value("${book-service}")
    private String bookService;

    @Value("${zuul-service}")
    private String zuulService;

    @GetMapping("/book/v1/{id}")
    public String getBook_V1(@PathVariable int id) {
        //Using hardcoded service url
        RestTemplate restTemplate = new RestTemplate();
        String baseUrl = "http://localhost:8501"; //Issue: If url of server changes, need to do code changes
        String url = baseUrl + "/book/v1/" + id;

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, getHeaders(), String.class);
        return response.getBody();
    }

    @GetMapping("/book/v2/{id}")
    public String getBook_V2(@PathVariable int id) {
        //Using Eureka Discovery Client
        RestTemplate restTemplate = new RestTemplate();

        List<ServiceInstance> instances = discoveryClient.getInstances(bookService);
        ServiceInstance serviceInstance = instances.get(0);
        String baseUrl = serviceInstance.getUri().toString();
        System.out.println("(V2) Base Url: " + baseUrl);
        String url = baseUrl + "/book/v1/" + id;

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, getHeaders(), String.class);
        return response.getBody();
    }

    @GetMapping("/book/v3/{id}")
    public String getBook_V3(@PathVariable int id) {
        //Using Eureka + Ribbon Load Balancer
        RestTemplate restTemplate = new RestTemplate();

        ServiceInstance serviceInstance = loadBalancerClient.choose(bookService); //Random available services
        String baseUrl = serviceInstance.getUri().toString();
        System.out.println("(V3) Base Url: " + baseUrl);
        String url = baseUrl + "/book/v1/" + id;

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, getHeaders(), String.class);
        return response.getBody();
    }

    @GetMapping("/book/v4/{id}")
    @HystrixCommand(fallbackMethod = "getFallBackBook")
    public String getBook_V4(@PathVariable int id) {
        //Using Eureka + Ribbon + Hystrix (server side)

        System.out.println("getBook_V4 with id: " + id);
        RestTemplate restTemplate = new RestTemplate();

        ServiceInstance serviceInstance = loadBalancerClient.choose(bookService); //Random available services
        String baseUrl = serviceInstance.getUri().toString();
        System.out.println("(V4) Base Url: " + baseUrl);
        String url = baseUrl + "/book/v1/" + id;

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, getHeaders(), String.class);
        return response.getBody();
    }

    // When there is an exception, return this Object
    public String getFallBackBook(int id) {
        System.out.println("getFallBackBook() with id: " + id);
        return "Book not available";
    }

    @GetMapping("/book/v5/{id}")
    public String getBook_V5(@PathVariable int id) {
        //Using Feign (Auto implement Eureka + Ribbon)
        System.out.println("getBook_V5 with id: " + id);

        return httpBookService.getBook(id);
    }

    @GetMapping("/book/v6/{id}")
    public String getBook_V6(@PathVariable int id) {
        //Using Zuul
        RestTemplate restTemplate = new RestTemplate();

        List<ServiceInstance> instances = discoveryClient.getInstances(zuulService);
        ServiceInstance serviceInstance = instances.get(0);
        String baseUrl = serviceInstance.getUri().toString();
        System.out.println("(V6) Base Url: " + baseUrl);
        String url = baseUrl + "/book-service" + "/book/v1/" + id;

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, getHeaders(), String.class);
        return response.getBody();
    }


    private HttpEntity<?> getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        return new HttpEntity<>(headers);
    }
}
