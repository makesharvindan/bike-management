package com.global.bike.config;

import com.global.bike.models.Sale;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;
@Configuration
@EnableKafka
public class KafkaConfig {

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {

        return new KafkaTemplate<>(kafkaProducer());
    }

    @Bean
    public ConsumerFactory<String,Sale> consumerFactory() {

        JsonDeserializer<Sale> deserializer = new JsonDeserializer<>(Sale.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String,Object> configs = new HashMap<>();
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configs.put(ConsumerConfig.GROUP_ID_CONFIG,"group2");
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"http://localhost:9092");
        return new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(),deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String,Sale> kafkaListenerFactory(){
        ConcurrentKafkaListenerContainerFactory<String,Sale> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    @Bean
    public ProducerFactory<String, Object> kafkaProducer () {

        Map<String,Object> configs = new HashMap<>();

        //for cloud karafka
//        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"moped.srvs.cloudkafka.com:9094");
//        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        configs.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,false);
//        configs.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG,"SASL_SSL");
//        configs.put(SaslConfigs.SASL_MECHANISM,"SCRAM-SHA-256");
//        configs.put(SaslConfigs.SASL_JAAS_CONFIG,"org.apache.kafka.common.security.scram.ScramLoginModule required username=\"wxufrcab\" password=\"VlBsxcBLwMxYuqQY1wrqwN10YDwlzE8b\";");

        //local kafka
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"http://localhost:9092");
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(configs);
    }
}
