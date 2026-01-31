package br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.utils;

import br.com.github.williiansilva51.zaldo.core.domain.Paginated;
import br.com.github.williiansilva51.zaldo.core.enums.DirectionOrder;
import br.com.github.williiansilva51.zaldo.core.enums.sort.SortField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PageUtils {
    public <T> Paginated<T> toDomainPage(Page<T> springPage) {
        return new Paginated<>(
                springPage.getContent(),
                springPage.getTotalElements(),
                springPage.getTotalPages(),
                springPage.getNumber());
    }

    public Pageable createPageRequest(int page, int size, SortField sort, DirectionOrder direction) {
        return PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.valueOf(direction.name()), sort.field()));
    }

}
