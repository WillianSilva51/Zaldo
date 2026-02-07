package br.com.github.williiansilva51.zaldo.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private String id;
    private String telegramId;
    private String name;
    private String email;
    private String password;

    public void update(User newInfo) {
        if (newInfo.getName() != null && !newInfo.getName().isBlank()) {
            name = newInfo.getName();
        }

        if (newInfo.getEmail() != null && !newInfo.getEmail().isBlank()) {
            email = newInfo.getEmail();
        }

        if (newInfo.getPassword() != null && !newInfo.getPassword().isBlank()) {
            password = newInfo.getPassword();
        }
    }
}
