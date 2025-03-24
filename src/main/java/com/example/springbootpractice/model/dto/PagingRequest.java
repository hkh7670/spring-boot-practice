package com.example.springbootpractice.model.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public record PagingRequest(
    Integer page,

    Integer size,

    Sort.Direction direction

) {

    private static final int MAX_PAGE_SIZE = 50;
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 10;


    public PagingRequest(Integer page, Integer size, Sort.Direction direction) {
        this.page = (page == null || page < 0) ? DEFAULT_PAGE : page;
        this.size = (size == null || size < 1) ? DEFAULT_PAGE_SIZE : Math.min(size, MAX_PAGE_SIZE);
        this.direction = direction == null ? Direction.DESC : direction;
    }

    public Pageable toPageable() {
        return PageRequest.of(
            this.page,
            this.size
        );
    }

    public Pageable toPageable(String... properties) {
        return PageRequest.of(
            this.page,
            this.size,
            this.direction,
            properties
        );
    }

}
