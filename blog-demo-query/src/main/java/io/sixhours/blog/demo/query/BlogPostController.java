package io.sixhours.blog.demo.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blog_posts")
public class BlogPostController {

    private static final Logger log = LoggerFactory.getLogger(BlogPostController.class);

    private final BlogPostQueryService blogPostQueryService;

    public BlogPostController(BlogPostQueryService blogPostQueryService) {
        this.blogPostQueryService = blogPostQueryService;
    }

    @GetMapping
    public void getAll() {
        log.info("Returning all blog posts.");
    }

    @GetMapping("/{aggregateId}")
    public void getByAggregateId(@PathVariable String aggregateId) {
        log.info("Returning blog post with id '{}'", aggregateId);
    }
}
