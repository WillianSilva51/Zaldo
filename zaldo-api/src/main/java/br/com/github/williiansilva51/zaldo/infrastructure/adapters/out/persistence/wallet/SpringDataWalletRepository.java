package br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.wallet;

import br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.entity.WalletEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface SpringDataWalletRepository extends JpaRepository<WalletEntity, Long> {
    Page<WalletEntity> findByUserId(String userId, Pageable pageable);

    @Query("""
                SELECT COALESCE(
                    SUM(
                        CASE
                            WHEN t.type = 'INCOME'  THEN t.amount
                            WHEN t.type = 'EXPENSE' THEN -t.amount
                        END
                    ), 0
                )
                FROM WalletEntity w
                LEFT JOIN w.transactions t
                WHERE w.id = :walletId
                  AND w.user.id = :userId
            """)
    BigDecimal getBalanceByWalletAndUser(
            @Param("walletId") Long walletId,
            @Param("userId") String userId
    );


}
