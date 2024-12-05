package com.kevin.bankmanagementsys.dto.response;

import com.kevin.bankmanagementsys.entity.BankName;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccListItem {
    Long id;
    String number;
    String bankName;

    public AccListItem(Long id, String content, BankName bankName) {
        this.id = id;
        this.number = content;
        this.bankName = bankName.name();
    }
}
