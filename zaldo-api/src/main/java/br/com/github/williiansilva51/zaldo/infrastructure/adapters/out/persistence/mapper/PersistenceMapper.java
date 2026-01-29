package br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.mapper;

public interface PersistenceMapper<En, D> {
    En toEntity(D domain);

    D toDomain(En entity);
}
