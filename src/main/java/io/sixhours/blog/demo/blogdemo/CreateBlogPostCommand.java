package io.sixhours.blog.demo.blogdemo;

public class CreateBlogPostCommand {

    private final String title;
    private final String body;
    private final String author;

    public CreateBlogPostCommand(String title, String body, String author) {
        this.title = title;
        this.body = body;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getAuthor() {
        return author;
    }
}
