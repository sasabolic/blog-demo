package io.sixhours.blog.demo.query;

import java.util.List;

public interface BlogPostQueryService {

    List<BlogPostResponse> findAll(String name);

    BlogPostResponse findById(Long id);
}
