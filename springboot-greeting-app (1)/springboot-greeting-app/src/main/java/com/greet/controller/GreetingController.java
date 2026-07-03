package com.greet.controller;
import com.greet.dto.GreetingRequestDto;
import com.greet.dto.GreetingResponseDto;
import com.greet.model.Greeting;
import com.greet.model.User;
import com.greet.repository.UserRepository;
import com.greet.service.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/greetings")
public class GreetingController {
    @Autowired
    private GreetingService greetingService;
    @Autowired
    private UserRepository userRepository;
    @GetMapping
    public ResponseEntity<List<GreetingResponseDto>> getAllGreetings() {
        List<GreetingResponseDto> greetings = greetingService.findAll().stream()
                .map(g -> GreetingResponseDto.builder()
                        .id(g.getId())
                        .message(g.getMessage())
                        .createdByUsername(g.getCreatedBy() != null ?
                                g.getCreatedBy().getUsername() : null)
                        .createdAt(g.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(greetings);
    }
    @PostMapping
    public ResponseEntity<?> createGreeting(
            @RequestBody GreetingRequestDto requestDto,
            @RequestAttribute("username") String username) {

        String message = requestDto.getMessage();
        if (message == null || message.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Message cannot be empty"));
        }
        User creator = userRepository.findByUsername(username).orElseThrow();
        Greeting greeting = new Greeting();
        greeting.setMessage(message);
        greeting.setCreatedBy(creator);
        Greeting saved = greetingService.save(greeting);

        GreetingResponseDto responseDto = GreetingResponseDto.builder()
                .id(saved.getId())
                .message(saved.getMessage())
                .createdByUsername(saved.getCreatedBy().getUsername())
                .createdAt(saved.getCreatedAt())
                .build();

        return ResponseEntity.status(201).body(responseDto);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateGreeting(
            @PathVariable("id") Long id,
            @RequestBody GreetingRequestDto requestDto) {

        String message = requestDto.getMessage();
        if (message == null || message.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Message cannot be empty"));
        }
        return greetingService.findById(id)
                .map(greeting -> {
                    greeting.setMessage(message);
                    Greeting updated = greetingService.save(greeting);

                    GreetingResponseDto responseDto =
                            GreetingResponseDto.builder()
                                    .id(updated.getId())
                                    .message(updated.getMessage())
                                    .createdByUsername(updated.getCreatedBy() != null ?
                                            updated.getCreatedBy().getUsername() : null)
                                    .createdAt(updated.getCreatedAt())
                                    .build();

                    return ResponseEntity.ok(responseDto);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGreeting(@PathVariable("id") Long id) {
        return greetingService.findById(id)
                .map(greeting -> {
                    greetingService.delete(id);
                    return ResponseEntity.ok(Map.of("message", "Greeting deleted successfully"));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
