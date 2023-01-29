package com.example.projectbackend.service;

import com.example.projectbackend.domain.Blog;
import com.example.projectbackend.domain.Member;
import com.example.projectbackend.domain.Product;
import com.example.projectbackend.dto.*;
import com.example.projectbackend.repository.BlogRepository;
import com.example.projectbackend.repository.ReplyRepository;
import com.example.projectbackend.repository.search.BlogSearch;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BlogServieImpl implements BlogService {
    private final BlogRepository blogRepository;
    private final ReplyRepository replyRepository;

    @Override
    public Long create(BlogDTO blogDTO) {
        Blog blog = dtoToEntity(blogDTO);
        return blogRepository.save(blog).getBid();
    }

    @Override
    public BlogDTO read(Long bid) {
        return null;
    }

    @Override
    public void update(BlogDTO blogDTO) {
        Optional<Blog> result = blogRepository.findById(blogDTO.getBid());
        Blog blog = result.orElseThrow();
        System.out.println("수정할 블로그 엔티티 찾음");
        System.out.println(blog);
        System.out.println(blogDTO);

        blog.changeCategory(blogDTO.getCategory());
        blog.changeText(blogDTO.getText());
        blog.changeTitle(blogDTO.getTitle());
        blog.changeFileName(blogDTO.getFileName());

        System.out.println(blog);

        System.out.println("수정 완료");
        blogRepository.save(blog);
    }

    @Override
    public void delete(Long bid) {
        replyRepository.deleteByBlog_Bid(bid);
        blogRepository.deleteById(bid);
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
        String category = pageRequestDTO.getCategory();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageableDesc("bid");

        Page<BlogWithReplyCountDTO> result = blogRepository.searchBlogPaging(category, keyword, pageable);
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

    @Override
    public BlogWithMemberAndReplyCountDTO readBlogByBid(Long bid) {
        List<Object[]> result = blogRepository.findBlogByBid(bid);
        Blog blog = (Blog) result.get(0)[0];
        Member member = (Member) result.get(0)[1];
        Long replyCount = (Long) result.get(0)[2];
        return objectToDTO2(blog, member, replyCount);
    }


}
