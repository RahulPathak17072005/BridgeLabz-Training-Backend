package com.greet.repository;
import com.greet.model.Greeting;
import org.springframework.data.jpa.repository.JpaRepository;
public interface GreetingRepository extends JpaRepository<Greeting, Long> {
}
