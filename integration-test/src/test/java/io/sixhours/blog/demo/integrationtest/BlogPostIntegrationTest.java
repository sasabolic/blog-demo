package io.sixhours.blog.demo.integrationtest;

import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.configuration.DockerComposeFiles;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import io.sixhours.blog.demo.command.BlogPost;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;

@RunWith(Cucumber.class)
@CucumberOptions(features = {"classpath:features"},
                    format = {"pretty", "html:build/reports/cucumber-report"},
                    glue = {"io.sixhours.blog.demo.integrationtest.steps"})
public class BlogPostIntegrationTest {

    static File file = new File(BlogPostIntegrationTest.class.getClassLoader().getResource("docker-compose.yml").getFile());

    @ClassRule
    public static DockerComposeRule docker = DockerComposeRule.builder()
            .files(new DockerComposeFiles(Collections.singletonList(file)))
            .build();

}