package io.sixhours.blog.demo.query;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@Service
public class BlogPostQueryServiceImpl implements BlogPostQueryService, InitializingBean, DisposableBean {
    private static final Logger log = LoggerFactory.getLogger(BlogPostQueryServiceImpl.class);

    private RestHighLevelClient client;

    @Override
    public void afterPropertiesSet() {
        client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));
    }

    public void destroy() {
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<BlogPostResponse> findAll(String name) {
        SearchRequest searchRequest = new SearchRequest("posts");
        searchRequest.types("doc");

        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest);
        } catch (IOException e) {
            log.error("SearchResponse: Error", e);
        }

        Stream.of(searchResponse.getHits().getHits())
                .forEach(h -> System.out.println(h.getSourceAsString()));

        return null;
    }

    @Override
    public BlogPostResponse findById(Long id) {
        return null;
    }
}
