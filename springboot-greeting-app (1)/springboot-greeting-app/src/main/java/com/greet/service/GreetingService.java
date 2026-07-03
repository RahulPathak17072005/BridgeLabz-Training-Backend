package com.greet.service;

import com.greet.model.Greeting;
import java.util.List;
import java.util.Optional;

public interface GreetingService {

    Greeting save(Greeting greeting);

    List<Greeting> findAll();

    Optional<Greeting> findById(Long id);

    void delete(Long id);
}
