package com.greet.dto;
import lombok.*;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GreetingResponseDto {
    private Long id;
    private String message;
    private String createdByUsername;
    private LocalDateTime createdAt;
}
