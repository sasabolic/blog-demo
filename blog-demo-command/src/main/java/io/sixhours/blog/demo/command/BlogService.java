package io.sixhours.blog.demo.command;

import java.util.UUID;

/**
 * Service responsible for updating BlogPost aggregate.
 *
 * @author Sasa Bolic
 */
public interface BlogService {

    /**
     * Creates blog post.
     *
     * @param title  the title
     * @param body   the body
     * @param author the author
     * @return the uuid
     */
    UUID create(String title, String body, String author);

    /**
     * Updates blog post.
     *
     * @param aggregateId the aggregate id
     * @param title       the title
     * @param body        the body
     * @param author      the author
     */
    void update(UUID aggregateId, String title, String body, String author);

    /**
     * Deletes blog post.
     *
     * @param aggregateId the aggregate id
     */
    void delete(UUID aggregateId);
}
