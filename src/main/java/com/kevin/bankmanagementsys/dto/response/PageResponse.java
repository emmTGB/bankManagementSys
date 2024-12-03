package com.kevin.bankmanagementsys.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PageResponse<T> {
    public PageResponse(List<T> content, int currentPage, int totalPages, int size, long totalElements) {
        this.content = content;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.size = size;
        this.totalElements = totalElements;
    }

    List<T> content;
    int currentPage;
    int totalPages;
    int size;
    long totalElements;

}
