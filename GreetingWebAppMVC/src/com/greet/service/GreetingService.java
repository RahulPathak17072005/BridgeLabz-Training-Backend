package com.greet.service;

import com.greet.model.Greeting;

public interface GreetingService {
    /**
     * Generates a greeting for the given user name.
     *
     * @param name the name of the user to greet
     * @return a Greeting object containing the personalized message
     */
    Greeting greet(String name);
}
