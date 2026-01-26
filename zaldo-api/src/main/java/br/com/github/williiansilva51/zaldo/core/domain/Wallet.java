package br.com.github.williiansilva51.zaldo.core.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "wallet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_id")
    private Long id;
}
