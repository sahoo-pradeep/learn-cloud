package com.example.bookclient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
public class BookRestController {

    private final DiscoveryClient discoveryClient;
    private final LoadBalancerClient loadBalancerClient;

    @Autowired
    public BookRestController(DiscoveryClient discoveryClient, LoadBalancerClient loadBalancerClient) {
        this.discoveryClient = discoveryClient;
        this.loadBalancerClient = loadBalancerClient;
    }

    public void getBook_V1() {
        RestTemplate restTemplate = new RestTemplate();
        String baseUrl = "http://localhost:8501"; //Issue: If url of server changes, need to do code changes
        String url = baseUrl + "/book/1";

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, getHeaders(), String.class);
            System.out.println("Response: " + response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getBook_V2() {
        //Using Eureka Discovery Client
        RestTemplate restTemplate = new RestTemplate();

        List<ServiceInstance> instances = discoveryClient.getInstances("book-service");
        ServiceInstance serviceInstance = instances.get(0);
        String baseUrl = serviceInstance.getUri().toString();
        System.out.println("(V2) Base Url: " + baseUrl);

        String url = baseUrl + "/book/1";
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, getHeaders(), String.class);
            System.out.println("Response: " + response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getBook_V3() {
        //Using Ribbon Load Balancer
        RestTemplate restTemplate = new RestTemplate();

        ServiceInstance serviceInstance = loadBalancerClient.choose("book-service");
        String baseUrl = serviceInstance.getUri().toString();
        System.out.println("(V3) Base Url: " + baseUrl);

        String url = baseUrl + "/book/1";
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, getHeaders(), String.class);
            System.out.println("Response: " + response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HttpEntity<?> getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        return new HttpEntity<>(headers);
    }
}
