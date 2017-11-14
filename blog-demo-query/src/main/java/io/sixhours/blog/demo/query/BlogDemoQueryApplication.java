package io.sixhours.blog.demo.command.blogdemoquery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Blog demo query application.
 *
 * @author Sasa Bolic
 */
@SpringBootApplication
public class BlogDemoQueryApplication {

	/**
	 * The entry point of application.
	 *
	 * @param args the input arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(BlogDemoQueryApplication.class, args);
		ExecutorService executor = Executors.newFixedThreadPool(1);

		final List<ConsumerLoop> consumers = new ArrayList<>();
		for (int i = 0; i < 1; i++) {
			ConsumerLoop consumer = new ConsumerLoop(i, "blog-demo-query", Collections.singletonList("blog-demo.post"));
			consumers.add(consumer);
			executor.submit(consumer);
		}

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				for (ConsumerLoop consumer : consumers) {
					consumer.shutdown();
				}
				executor.shutdown();
				try {
					executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
