package io.sixhours.blog.demo.query;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class KafkaInitializer implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        KafkaConsumerService consumer = new KafkaConsumerService("blog-demo-query", Collections.singletonList("blog-demo.post"));
        executor.submit(consumer);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            consumer.shutdown();
            executor.shutdown();
            try {
                executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));
    }
}
