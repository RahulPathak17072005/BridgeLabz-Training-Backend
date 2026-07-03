package com.greet.service;
import com.greet.model.Greeting;
import com.greet.repository.GreetingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class GreetingServiceImpl implements GreetingService {
    @Autowired
    private GreetingRepository greetingRepository;
    @Override
    public Greeting save(Greeting greeting) {
        log.info("Saving new greeting: '{}'", greeting.getMessage());
        return greetingRepository.save(greeting);
    }
    @Override
    public List<Greeting> findAll() {
        log.debug("Retrieving all greetings from H2 database");
        return greetingRepository.findAll();
    }
    @Override
    public Optional<Greeting> findById(Long id) {
        log.debug("Querying greeting by ID: {}", id);
        return greetingRepository.findById(id);
    }
    @Override
    public void delete(Long id) {
        log.warn("Deleting greeting with ID: {}", id);
        greetingRepository.deleteById(id);
    }
}