package com.example.bookclient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
public class BookRestController {

    private DiscoveryClient discoveryClient;

    @Autowired
    public BookRestController(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    public void getBook() {
        RestTemplate restTemplate = new RestTemplate();
        //String baseUrl = "http://localhost:8501"; //Issue: If url of server changes, need to do code changes

        List<ServiceInstance> instances = discoveryClient.getInstances("book-service");
        ServiceInstance serviceInstance = instances.get(0);
        String baseUrl = serviceInstance.getUri().toString();

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
