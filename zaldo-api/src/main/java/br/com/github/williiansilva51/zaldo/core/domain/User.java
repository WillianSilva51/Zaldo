package br.com.github.williiansilva51.zaldo.core.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    public void update(User newInfo) {
        if (newInfo.getName() != null) {
            name = newInfo.getName();
        }
        if (newInfo.getEmail() != null) {
            email = newInfo.getEmail();
        }
        if (newInfo.getPassword() != null) {
            password = newInfo.getPassword();
        }
    }
}
