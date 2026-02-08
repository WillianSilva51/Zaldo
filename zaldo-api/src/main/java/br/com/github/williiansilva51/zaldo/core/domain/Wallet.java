package br.com.github.williiansilva51.zaldo.core.domain;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wallet implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

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
            description = newInfo.getDescription().isBlank()
                    ? null
                    : newInfo.getDescription();
        }
    }
}
