package io.sixhours.blog.demo.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

/**
 * Controller for write BlogPost requests.
 *
 * @author Sasa Bolic
 */
@RestController
@RequestMapping("/blog_posts")
public class BlogPostController {
    private static final Logger log = LoggerFactory.getLogger(BlogPostController.class);

    private final BlogService blogService;

    /**
     * Instantiates a new {@code BlogPostController}.
     *
     * @param blogService the blog service
     */
    public BlogPostController(BlogService blogService) {
        this.blogService = blogService;
    }

    /**
     * Creates blog post.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<Void> createBlogPost(@RequestBody BlogPostRequest request) {
        log.info("Creating blog: '{}'",  request);

        final UUID aggregateId = blogService.create(request.getTitle(), request.getBody(), request.getAuthor());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
                "/{aggregateId}").buildAndExpand(aggregateId).toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * Updates blog post.
     *
     * @param aggregateId the aggregate id
     * @param request     the request
     * @return the response entity
     */
    @PutMapping("/{aggregateId}")
    public ResponseEntity<Void> updateBlogPost(@PathVariable UUID aggregateId, @RequestBody BlogPostRequest request) {
        log.info("Updating blog '{}' with: '{}'", aggregateId, request);

        blogService.update(aggregateId, request.getTitle(), request.getBody(), request.getAuthor());

        return ResponseEntity.ok().build();
    }

    /**
     * Deletes blog post.
     *
     * @param aggregateId the aggregate id
     * @return the response entity
     */
    @DeleteMapping("/{aggregateId}")
    public ResponseEntity<Void> deleteBlogPost(@PathVariable UUID aggregateId) {
        log.info("Deleting blog '{}'", aggregateId);

        blogService.delete(aggregateId);

        return ResponseEntity.accepted().build();
    }
}
