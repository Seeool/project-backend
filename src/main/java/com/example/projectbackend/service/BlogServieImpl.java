package com.example.projectbackend.service;

import com.example.projectbackend.domain.Blog;
import com.example.projectbackend.dto.BlogDTO;
import com.example.projectbackend.dto.BlogWithReplyCountDTO;
import com.example.projectbackend.dto.PageRequestDTO;
import com.example.projectbackend.dto.PageResponseDTO;
import com.example.projectbackend.repository.BlogRepository;
import com.example.projectbackend.repository.search.BlogSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogServieImpl implements BlogService {
    private final BlogRepository blogRepository;

    @Override
    public Long create(BlogDTO blogDTO) {
        return null;
    }

    @Override
    public BlogDTO read(Long bid) {
        return null;
    }

    @Override
    public void update(BlogDTO blogDTO) {

    }

    @Override
    public void delete(Long bid) {

    }

    @Override
    public int getAllCount() {
        return (int) blogRepository.count();
    }

    @Override
    public int getCountBlogsByCategory(int category) {
        return blogRepository.countBlogByCategoryIs(category);
    }

    @Override
    public PageResponseDTO<BlogWithReplyCountDTO> getBlogsPagination(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();
        String category = pageRequestDTO.getCategory();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageableDesc("bid");

        Page<BlogWithReplyCountDTO> result = blogRepository.searchBlogPaging(types, category, keyword, pageable);
        List<BlogWithReplyCountDTO> dtoList = result.getContent();
        return new PageResponseDTO<>(pageRequestDTO, dtoList, (int) result.getTotalElements());
    }

    @Override
    public List<BlogWithReplyCountDTO> get3BlogsOrderByRegDateWithReplyCount() {
        List<Object[]> result = blogRepository.findFirst3ByOrderByRegDateDescWithReplyCount();
        return result.stream().map(object -> {
            Blog blog = (Blog) object[0];
            Long replyCount = (Long) object[1];
            return objectToDTO(blog, replyCount);
        }).toList();
    }


}
