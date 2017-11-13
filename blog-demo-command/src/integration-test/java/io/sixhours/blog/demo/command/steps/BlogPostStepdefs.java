package io.sixhours.blog.demo.command.steps;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.sixhours.blog.demo.command.*;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Iterator;
import java.util.Properties;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;

public class BlogPostStepdefs {

    private BlogPost blogPost;
    private Consumer<String, byte[]> consumer;
    private final String bootStrapServers = "localhost:9092";

    @Before
    public void before() {
        blogPost = new BlogPost(new KafkaEventService(bootStrapServers));

        Properties p = new Properties();
        p.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
        p.put(ConsumerConfig.GROUP_ID_CONFIG, "blog-demo-command-itest");
        p.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        p.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());

        consumer = new KafkaConsumer<>(p);
        consumer.subscribe(Collections.singletonList("blog-demo.post"));
    }

    @Given("^empty post topic$")
    public void empty_post_opic() throws Throwable {
        consumer.poll(1000);
    }

    @When("^command to create post is executed (\\d+) times$")
    public void command_to_create_post_is_executed_times(int size) throws Throwable {
        for (int i = 0; i < size; i++) {
            blogPost.process(new CreateBlogPostCommand("Title", "Body", "Author"));
        }
    }

    @When("^command to create post is executed$")
    public void command_to_create_post_is_executed() {
        blogPost.process(new CreateBlogPostCommand("Title", "Body", "Author"));
    }

    @When("^command to update post is executed$")
    public void command_to_update_post_is_executed() throws Throwable {
        blogPost.process(new UpdateBlogPostCommand(UUID.randomUUID(), "TitleUpdated", "BodyUpdated", "AuthorUpdated"));
    }

    @When("^command to delete post is executed$")
    public void command_to_delete_post_is_executed() throws Throwable {
        blogPost.process(new DeleteBlogPostCommand(UUID.randomUUID()));
    }

    @Then("^the number of messages in topic should be (\\d+)$")
    public void the_number_of_messages_in_topic_is_and_message_key_has_value_as_event_id(int size) throws Throwable {
        ConsumerRecords<String, byte[]> consumerRecords = consumer.poll(1000);

        assertThat(consumerRecords.count(), equalTo(size));
    }

    @Then("^message key should have value as event id$")
    public void message_key_should_have_value_as_event_id() throws Throwable {
        ConsumerRecords<String, byte[]> consumerRecords = consumer.poll(1000);
        ConsumerRecord<String, byte[]> consumerRecord = consumerRecords.iterator().next();

        assertThat(blogPost, hasProperty("id", equalTo(UUID.fromString(consumerRecord.key()))));
    }

    @Then("^header with name (.*) should have value (.*)$")
    public void header_with_name_schema_should_have_value_blog_post_created_avsc(String headerName, String headerValue) throws Throwable {
        ConsumerRecords<String, byte[]> consumerRecords = consumer.poll(1000);

        ConsumerRecord<String, byte[]> consumerRecord = consumerRecords.iterator().next();
        byte[] schema = consumerRecord.headers().lastHeader(headerName).value();

        assertThat(new String(schema), equalTo(headerValue));
    }

    @When("^command to create, update, delete post is executed$")
    public void command_to_create_update_delete_post_is_executed() throws Throwable {
        blogPost.process(new CreateBlogPostCommand("Title", "Body", "Author"));
        blogPost.process(new UpdateBlogPostCommand(blogPost.getId(), "TitleUpdated", "BodyUpdated", "AuthorUpdated"));
        blogPost.process(new DeleteBlogPostCommand(blogPost.getId()));
    }

    @Then("^order of events should have header (.*) in order (.*), (.*), (.*)$")
    public void order_of_events_is_create_update_delete(String headerName, String created, String updated, String deleted) throws Throwable {
        ConsumerRecords<String, byte[]> consumerRecords = consumer.poll(1000);

        assertThat(consumerRecords.count(), equalTo(3));

        Iterator<ConsumerRecord<String, byte[]>> iterator = consumerRecords.iterator();
        assertThat(new String(iterator.next().headers().lastHeader(headerName).value()), equalTo(created));
        assertThat(new String(iterator.next().headers().lastHeader(headerName).value()), equalTo(updated));
        assertThat(new String(iterator.next().headers().lastHeader(headerName).value()), equalTo(deleted));
    }

    @After
    public void after() throws InterruptedException {
        consumer.close();
    }


}
