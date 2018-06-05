package io.sixhours.blog.demo.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class KafkaInitializer implements ApplicationListener<ApplicationReadyEvent>, DisposableBean {
    private static final Logger log = LoggerFactory.getLogger(KafkaInitializer.class);

    private List<KafkaConsumerService> consumers = new ArrayList<>();

    @Value("${consumer.number:1}")
    private int numberOfThreads;

    private ExecutorService executor;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("STARTING Consumer threads.");
        executor = Executors.newFixedThreadPool(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            KafkaConsumerService consumer = new KafkaConsumerService("blog-demo-query", Collections.singletonList("blog-demo.post"));
            consumers.add(consumer);

            executor.execute(consumer);
        }

    }

    public void destroy() {
        log.info("DESTROYING Consumer threads.");
        consumers.forEach(consumer -> consumer.shutdown());
        if (executor != null) {
            executor.shutdown();
            try {
                executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
