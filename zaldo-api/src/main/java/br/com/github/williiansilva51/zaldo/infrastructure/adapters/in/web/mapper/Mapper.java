package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.mapper;

public interface Mapper<D, Req, CRes, URes> {
    D toDomain(Req request);

    D toDomainByUpdate(URes request);

    CRes toResponse(D domain);
}
