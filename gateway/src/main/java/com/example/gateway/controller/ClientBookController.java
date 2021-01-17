package com.example.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class ClientBookController {

    private final DiscoveryClient discoveryClient;
    private final LoadBalancerClient loadBalancerClient;

    @Autowired
    public ClientBookController(DiscoveryClient discoveryClient, LoadBalancerClient loadBalancerClient) {
        this.discoveryClient = discoveryClient;
        this.loadBalancerClient = loadBalancerClient;
    }

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

        List<ServiceInstance> instances = discoveryClient.getInstances("book-service");
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

        ServiceInstance serviceInstance = loadBalancerClient.choose("book-service"); //Random available services
        String baseUrl = serviceInstance.getUri().toString();
        System.out.println("(V3) Base Url: " + baseUrl);
        String url = baseUrl + "/book/v1/" + id;

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, getHeaders(), String.class);
        return response.getBody();
    }

    @GetMapping("/book/v4/{id}")
    public String getBook_V4(@PathVariable int id) {
        //Using Eureka + Ribbon + Hystrix (server side)
        RestTemplate restTemplate = new RestTemplate();

        ServiceInstance serviceInstance = loadBalancerClient.choose("book-service"); //Random available services
        String baseUrl = serviceInstance.getUri().toString();
        System.out.println("(V4) Base Url: " + baseUrl);
        String url = baseUrl + "/book/v2/" + id;

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, getHeaders(), String.class);
        return response.getBody();
    }

    private HttpEntity<?> getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        return new HttpEntity<>(headers);
    }
}
