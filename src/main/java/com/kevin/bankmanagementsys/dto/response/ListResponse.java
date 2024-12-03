package com.kevin.bankmanagementsys.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
public class ListResponse<T> {
    List<T> content;
    long total;
}