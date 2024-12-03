package com.kevin.bankmanagementsys.dto.response;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ListItem {
    Long id;
    String content;

    public ListItem(Long id, String content) {
        this.id = id;
        this.content = content;
    }
}
