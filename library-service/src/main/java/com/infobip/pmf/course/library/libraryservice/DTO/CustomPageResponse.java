package com.infobip.pmf.course.library.libraryservice.DTO;

import org.springframework.data.domain.Page;

import java.util.List;

public class CustomPageResponse<T> {
    private final List<T> results;
    private final int page;
    private final int size;
    private final int totalPages;
    private final long totalResults;

    public CustomPageResponse(Page<T> pagination) {
        this.results = pagination.getContent();
        this.page = pagination.getNumber();
        this.size = pagination.getSize();
        this.totalPages = pagination.getTotalPages();
        this.totalResults = pagination.getTotalElements();
    }

    public List<T> getResults() {
        return results;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalResults() {
        return totalResults;
    }
}
