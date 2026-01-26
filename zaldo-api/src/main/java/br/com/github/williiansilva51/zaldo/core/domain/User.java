package br.com.github.williiansilva51.zaldo.core.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity()
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

    private String name;

    private String email;

    private String password;
}
