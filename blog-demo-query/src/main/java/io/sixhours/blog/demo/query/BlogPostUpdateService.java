package io.sixhours.blog.demo.query;

import io.sixhours.blog.demo.common.BlogPostCreated;
import io.sixhours.blog.demo.common.BlogPostDeleted;
import io.sixhours.blog.demo.common.BlogPostUpdated;
import org.apache.http.HttpHost;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

public class BlogPostUpdateService {
    private static final Logger log = LoggerFactory.getLogger(BlogPostUpdateService.class);

    private RestHighLevelClient client;


    public BlogPostUpdateService() {
        init();
    }

    @PostConstruct
    public void init() {
        client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));
    }

    public void create(BlogPostCreated event) {
        log.info("Creating new blog post... '{}'", event.getAggregateId());

        IndexRequest request = new IndexRequest("posts", "doc", event.getAggregateId().toString())
                .source("title", event.getTitle(),
                        "body", event.getBody(),
                        "author", event.getAuthor(),
                        "dateCreated", event.getDateCreated());

        try {
            IndexResponse response = client.index(request);
            log.info("IndexResponse: {}", response);
        } catch (Exception e) {
            log.error("IndexResponse: Error", e);
        }

    }

    public void update(BlogPostUpdated event) {
        log.info("Updating blog post... '{}'", event.getAggregateId());

        UpdateRequest request = new UpdateRequest("posts", "doc", event.getAggregateId().toString())
                .doc("title", event.getTitle(),
                        "body", event.getBody(),
                        "author", event.getAuthor());

        try {
            log.info("BEGIN - SALE SALE SALE SALE SALE");
            UpdateResponse response = client.update(request);
            log.info("END - SALE SALE SALE SALE SALE");
            log.info("UpdateResponse: {}", response);
        } catch (Exception e) {
            log.error("UpdateResponse: Error", e);
        }


    }

    public void delete(BlogPostDeleted event) {
        log.info("Deleting blog post... '{}", event.getAggregateId());

        DeleteRequest request = new DeleteRequest("posts", "doc", event.getAggregateId().toString());

        try {
            DeleteResponse response = client.delete(request);
            if (response.getResult() == DocWriteResponse.Result.NOT_FOUND) {
                log.warn("Document '{}' marked as DELETED was not found", event.getAggregateId());
            }

            log.info("DeleteResponse: {}", response);
        } catch (Exception e) {
            log.error("DeleteResponse: Error", e);
        }
    }

    @PreDestroy
    public void destroy() {
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
