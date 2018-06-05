package io.sixhours.blog.demo.query;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class BlogPostQueryServiceTest {

    @TestConfiguration
    @ComponentScan({"io.sixhours.blog.demo.query"})
    static class TestConfig {

    }

    @Autowired
    private BlogPostQueryService queryService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void whenFindAllThenReturnResult() {
        queryService.findAll(null);

    }
}
