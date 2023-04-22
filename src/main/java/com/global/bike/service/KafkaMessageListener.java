package com.global.bike.service;

import com.global.bike.exception.InternalServerErrorException;
import com.global.bike.models.Sale;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessageListener {

    @KafkaListener(topics = "test-1", groupId = "group1", containerFactory = "kafkaListenerFactory")
    public void listener(Sale s, Acknowledgment ack) throws InternalServerErrorException {
        try{

            System.out.println(s.getId());
            //try to save event
            if(ack != null)
                ack.acknowledge();
        } catch (Exception e) {
            throw new InternalServerErrorException("Exception in saving kafka message"+e.getMessage());
        }


    }
}
