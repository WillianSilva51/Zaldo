package br.com.github.williiansilva51.zaldo.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wallet {
    private Long id;
    private String name;
    private String description;
    private User user;

    void update(Wallet newInfo) {
        if (newInfo.getName() != null && !newInfo.getName().isBlank()) {
            name = newInfo.getName();
        }
        if (newInfo.getDescription() != null && !newInfo.getDescription().isBlank()) {
            description = newInfo.getDescription();
        }
    }
}
