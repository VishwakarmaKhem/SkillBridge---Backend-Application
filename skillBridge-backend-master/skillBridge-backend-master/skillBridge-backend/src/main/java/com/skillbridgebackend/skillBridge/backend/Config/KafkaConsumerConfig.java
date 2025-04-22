package com.skillbridgebackend.skillBridge.backend.Config;

import com.skillbridgebackend.skillBridge.backend.Dto.FeedbackDto;
import com.skillbridgebackend.skillBridge.backend.Dto.KafkaProducerDto;
import com.skillbridgebackend.skillBridge.backend.Entity.Feedback;
import com.skillbridgebackend.skillBridge.backend.Utils.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@Slf4j
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, KafkaProducerDto> consumerFactory() {
        try (JsonDeserializer<KafkaProducerDto> deserializer = new JsonDeserializer<>(KafkaProducerDto.class)) {
            deserializer.setRemoveTypeHeaders(false);
            deserializer.addTrustedPackages("*");
            deserializer.setUseTypeMapperForKey(true);
            final Map<String, Object> configs = new ConcurrentHashMap<>();
            configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
            DefaultKafkaConsumerFactory<String, KafkaProducerDto> defaultKafkaConsumerFactory;
            try (StringDeserializer stringDeserializer = new StringDeserializer()) {
                defaultKafkaConsumerFactory = new DefaultKafkaConsumerFactory<>(configs,
                        stringDeserializer, deserializer);
            }
            return defaultKafkaConsumerFactory;
        }
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, KafkaProducerDto> kafkaListenerContainerFactory() {
        final ConcurrentKafkaListenerContainerFactory<String, KafkaProducerDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setBatchListener(true);
        return factory;
    }


}
