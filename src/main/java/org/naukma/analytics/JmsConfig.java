package org.naukma.analytics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.jms.ConnectionFactory;
import org.naukma.analytics.analytics.AnalyticsEvent;
import org.naukma.analytics.booking.BookingEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@EnableJms
public class JmsConfig {

    // Queue (P2P)
    @Bean
    public JmsListenerContainerFactory<?> jmsListenerFactory(@Qualifier("jmsConnectionFactory") ConnectionFactory connectionFactory,
                                                             DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    // Topic (Pub/Sub)
    @Bean
    public JmsListenerContainerFactory<?> jmsTopicListenerFactory(@Qualifier("jmsConnectionFactory") ConnectionFactory connectionFactory,
                                                                  DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setPubSubDomain(true);
        return factory;
    }

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("JMS_TYPE");

        Map<String, Class<?>> typeIdMappings = new HashMap<>();
        typeIdMappings.put("JMS_TYPE", AnalyticsEvent.class);
        typeIdMappings.put("BookingEvent", BookingEvent.class);
        typeIdMappings.put("AnalyticsEvent", AnalyticsEvent.class);
        converter.setTypeIdMappings(typeIdMappings);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        converter.setObjectMapper(objectMapper);

        return converter;
    }
}
