package com.global.bike.config;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.global.bike.models.Bike;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
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
public class KafkaConfig {

    @Bean
    public KafkaTemplate<String,String> kafkaTemplate() {

        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ProducerFactory<String, String> producerFactory () {

        Map<String,Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"moped.srvs.cloudkafka.com:9094");
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,false);
        configs.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG,"SASL_SSL");
        configs.put(SaslConfigs.SASL_MECHANISM,"SCRAM-SHA-256");
        configs.put(SaslConfigs.SASL_JAAS_CONFIG,"org.apache.kafka.common.security.scram.ScramLoginModule required username=\"wxufrcab\" password=\"VlBsxcBLwMxYuqQY1wrqwN10YDwlzE8b\";");
        return new DefaultKafkaProducerFactory<>(configs);
    }
}
