package com.skillbridgebackend.skillBridge.backend.Config;

import com.skillbridgebackend.skillBridge.backend.Dto.KafkaProducerDto;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public Map<String, Object> producerConfigs(){
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return props;
    }

    @Bean
    public ProducerFactory<String, KafkaProducerDto> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, KafkaProducerDto> kafkaTemplate(){
//        KafkaTemplate<String, KafkaProducerDto> kafkaTemplate = null;
//        try {
//            kafkaTemplate=new KafkaTemplate<>(producerFactory());
//        }catch (Exception e){
//            System.out.println("error while creating the kafka template");
//        }
//        return kafkaTemplate;
        return new KafkaTemplate<>(producerFactory());
    }




}
