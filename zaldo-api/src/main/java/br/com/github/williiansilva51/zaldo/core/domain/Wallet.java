package br.com.github.williiansilva51.zaldo.core.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity()
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "wallet")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_id")
    private Long id;
}
