package io.sixhours.blog.demo.command;

/**
 * Command used to create blog post.
 *
 * @author Sasa Bolic
 */
public class CreateBlogPostCommand {

    private final String title;
    private final String body;
    private final String author;

    /**
     * Instantiates a new {@code CreateBlogPostCommand}.
     *
     * @param title  the title
     * @param body   the body
     * @param author the author
     */
    public CreateBlogPostCommand(String title, String body, String author) {
        this.title = title;
        this.body = body;
        this.author = author;
    }

    /**
     * Returns title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns body.
     *
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * Returns author.
     *
     * @return the author
     */
    public String getAuthor() {
        return author;
    }
}
