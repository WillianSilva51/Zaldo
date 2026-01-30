package br.com.github.williiansilva51.zaldo.core.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wallet {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private User user;

    public void update(Wallet newInfo) {
        if (newInfo.getName() != null && !newInfo.getName().isBlank()) {
            name = newInfo.getName();
        }
        if (newInfo.getDescription() != null) {
            description = newInfo.getDescription();
        }
    }
}
