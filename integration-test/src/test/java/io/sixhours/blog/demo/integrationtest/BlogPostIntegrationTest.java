package io.sixhours.blog.demo.integrationtest;

import com.palantir.docker.compose.DockerComposeRule;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.ClassRule;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = {"classpath:features"},
                    format = {"pretty", "html:build/reports/cucumber-report"},
                    glue = {"io.sixhours.blog.demo.integrationtest.steps"})
public class BlogPostITest {

    @ClassRule
    public static DockerComposeRule docker = DockerComposeRule.builder()
            .file("docker-compose.yml")
            .build();

}