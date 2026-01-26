package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.mapper;

public interface Mapper<D, Req, Res> {
    D toDomain(Req request);

    Res toResponse(D domain);
}
