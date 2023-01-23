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
    private String keyword;
    private String sort;

    public Pageable getPageableDesc(String sort) {
        return PageRequest.of(this.page - 1, this.size, Sort.by(sort).descending());
    }
    public Pageable getPageableAsc(String sort) {
        return PageRequest.of(this.page - 1, this.size, Sort.by(sort).ascending());
    }
}
