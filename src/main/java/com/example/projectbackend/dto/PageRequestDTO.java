package com.example.projectbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageRequestDTO {
    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 9;

    private String category;
    private String type;
    private String keyword;
    private String sort;
    public String[] getTypes() {
        if(type == null || type.isEmpty()) {
            return null;
        }
        return type.split("");
    }
    public Pageable getPageable(String sort) {
        switch (sort) {
            case "" :
                return PageRequest.of(this.page - 1, this.size, Sort.by("salesVolume").descending());
            case "0" :
                return PageRequest.of(this.page - 1, this.size, Sort.by("salesVolume").descending());
            case "1" :
                return PageRequest.of(this.page - 1, this.size, Sort.by("salesVolume").ascending());
            case "2" :
                return PageRequest.of(this.page - 1, this.size, Sort.by("price").descending());
            case "3" :
                return PageRequest.of(this.page - 1, this.size, Sort.by("price").ascending());
            case "4" :
                return PageRequest.of(this.page - 1, this.size, Sort.by("regDate").descending());
            case "5" :
                return PageRequest.of(this.page - 1, this.size, Sort.by("regDate").ascending());
        }
        return null;
    }
}
