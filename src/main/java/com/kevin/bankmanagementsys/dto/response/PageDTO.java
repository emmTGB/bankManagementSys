package com.kevin.bankmanagementsys.dto.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PageDTO<T> {
    public PageDTO(List<T> content, int currentPage, int totalPages, int size, long totalElements) {
        this.content = content;
        this.currentPae = currentPage;
        this.totalPages = totalPages;
        this.size = size;
        this.totalElements = totalElements;
    }

    List<T> content;
    int currentPae;
    int totalPages;
    int size;
    long totalElements;

}
