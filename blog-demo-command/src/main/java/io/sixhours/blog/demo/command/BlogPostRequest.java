package io.sixhours.blog.demo.command;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BlogPostRequest {

    private String title;
    private String body;
    private String author;

    @JsonCreator
    public BlogPostRequest(
            @JsonProperty("title") String title,
            @JsonProperty("body") String body,
            @JsonProperty("author") String author) {
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

    @Override
    public String toString() {
        return "CreateBlogPostRequest{" +
                "title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
