package com.greet.service;

import com.greet.model.Greeting;

import java.util.List;

public interface GreetingService {

    List<Greeting> getAllGreetings();

    Greeting getGreetingById(int id);

    boolean createGreeting(Greeting greeting);

    boolean updateGreeting(Greeting greeting);

    boolean deleteGreeting(int id);
}