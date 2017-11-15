package io.sixhours.blog.demo.command;

import io.sixhours.blog.demo.common.BlogPostDeleted;
import io.sixhours.blog.demo.common.Event;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;

/**
 * Handles {@link BlogPostDeleted} events.
 *
 * @author Sasa Bolic
 */
public class BlogPostDeletedHandler implements EventHandler {
    private static final Logger log = LoggerFactory.getLogger(BlogPostDeletedHandler.class);

    private EventHandler next;
    private static final String SCHEMA = "blog_post_deleted.avsc";
    private AvroService avroService = new AvroService("schemas/" + SCHEMA);;

    @Override
    public void setNext(EventHandler handler) {
        this.next = Objects.requireNonNull(handler, "Handler cannot be null");
    }

    @Override
    public ProducerRecord encode(Event event) {
        Objects.requireNonNull(event, "Event must not be null");

        if (event instanceof BlogPostDeleted) {
            log.debug("Event BlogPostDeleted: '{}'", ((BlogPostDeleted) event).getAggregateId());

            Map<String, Object> data = Map.of("aggregate_id", ((BlogPostDeleted) event).getAggregateId().toString());

            ProducerRecord<String, byte[]> rec =
                    new ProducerRecord<>(event.getTopicName(), event.getAggregateId().toString(), avroService.encode(data));

            rec.headers().add("schema", SCHEMA.getBytes());
            rec.headers().add("class", BlogPostDeleted.class.getCanonicalName().getBytes());

            return rec;
        } else if (this.next != null) {
            return this.next.encode(event);
        } else {
            throw new RuntimeException("Event is left unhandled");
        }
    }

}