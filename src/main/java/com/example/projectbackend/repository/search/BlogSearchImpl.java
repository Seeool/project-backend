package com.example.projectbackend.repository.search;

import com.example.projectbackend.domain.Blog;
import com.example.projectbackend.domain.QBlog;
import com.example.projectbackend.domain.QReply;
import com.example.projectbackend.dto.BlogDTO;
import com.example.projectbackend.dto.BlogWithReplyCountDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

public class BlogSearchImpl extends QuerydslRepositorySupport implements BlogSearch {
    public BlogSearchImpl() {
        super(Blog.class);
    }

    @Override
    public Page<BlogWithReplyCountDTO> searchBlogPaging(String category, String keyword, Pageable pageable) {
        QBlog blog = QBlog.blog;
        QReply reply = QReply.reply;
        JPQLQuery<Blog> query = from(blog);
        query.leftJoin(reply).on(reply.blog.eq(blog));
        query.groupBy(blog);

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(!category.isEmpty()) {
            booleanBuilder.or(blog.category.eq(Integer.parseInt(category)));
        }

        booleanBuilder.and(blog.title.contains(keyword));

        query.where(booleanBuilder);

        this.getQuerydsl().applyPagination(pageable, query);

        JPQLQuery<Tuple> tupleJPQLQuery = query.select(blog, reply.countDistinct());
        List<Tuple> tupleList = tupleJPQLQuery.fetch();

        List<BlogWithReplyCountDTO> dtoList = tupleList.stream().map(tuple -> {
            Blog blog1 = tuple.get(blog);
            Long replyCount = tuple.get(reply.countDistinct());
            BlogWithReplyCountDTO dto = BlogWithReplyCountDTO.builder()
                    .mid(blog1.getMember().getId())
                    .bid(blog1.getBid())
                    .fileName(blog1.getFileName())
                    .title(blog1.getTitle())
                    .text(blog1.getText())
                    .regDate(blog1.getRegDate())
                    .modDate(blog1.getModDate())
                    .category(blog1.getCategory())
                    .replyCount(replyCount)
                    .build();
            return dto;
        }).toList();
        long count = query.fetchCount();

        return new PageImpl<>(dtoList, pageable, count);
    }
}
