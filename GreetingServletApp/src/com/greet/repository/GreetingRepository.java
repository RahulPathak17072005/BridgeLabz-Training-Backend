package com.greet.repository;

import com.greet.model.Greeting;

import java.util.List;

public interface GreetingRepository {

    List<Greeting> findAll();

    Greeting findById(int id);

    boolean save(Greeting greeting);

    boolean update(Greeting greeting);

    boolean delete(int id);
}