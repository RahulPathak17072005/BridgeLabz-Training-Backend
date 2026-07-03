package com.greet.service;
import com.greet.model.Greeting;

public class GreetingServiceImpl implements GreetingService {

    private String prefix;

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void init() {
        System.out.println("=== GreetingServiceImpl initialized ===");
        System.out.println("prefix = " + prefix);
    }

    public void destroy() {
        System.out.println("=== GreetingServiceImpl destroyed ===");
    }

    @Override
    public Greeting greet(String name) {
        return new Greeting(prefix + ", " + name + "!");
    }
}