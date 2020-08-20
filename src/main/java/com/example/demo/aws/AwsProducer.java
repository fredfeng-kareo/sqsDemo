package com.example.demo.aws;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.*;
import com.example.demo.payload.DemoMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class AwsProducer {
    @Value("${queue.stand}")
    String QUEUE_NAME;

    @Autowired
    public AmazonSQSClientBuilder amazonSQSClientBuilder;

    @Autowired
    ObjectMapper objectMapper;

    public void publishMessage() throws JsonProcessingException {
        final AmazonSQS sqs =  amazonSQSClientBuilder.build();
        DemoMessage payload = new DemoMessage(UUID.randomUUID().toString(),"fifo queue " + total++,new Date());

        String queueUrl = sqs.getQueueUrl(QUEUE_NAME).getQueueUrl();

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(objectMapper.writeValueAsString(payload))
                .withDelaySeconds(5);
        sqs.sendMessage(send_msg_request);
    }

       private int total = 1;
    @Scheduled(fixedRate = 1000L)
    public void sendMessage() throws JsonProcessingException {
        if(total > 20) {
            return;
        }
        publishMessage();
    }

}
