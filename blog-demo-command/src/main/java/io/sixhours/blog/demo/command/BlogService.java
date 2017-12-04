package io.sixhours.blog.demo.command;

import java.util.UUID;

public interface BlogService {

    UUID create(String title, String body, String author);

    void update(UUID aggregateId, String title, String body, String author);

    void delete(UUID aggregateId);
}
