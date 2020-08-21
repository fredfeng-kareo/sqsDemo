package com.example.demo.aws;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.example.demo.payload.SurescriptMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AwsConsumer {
    @Value("${queue.fifo}")
    String QUEUE_NAME;

    @Autowired
    public AmazonSQSClientBuilder amazonSQSClientBuilder;

    @Autowired
    ObjectMapper objectMapper;

    public void receiveMessage() throws JsonProcessingException {
        final AmazonSQS sqs =  amazonSQSClientBuilder.build();
        String queueUrl = sqs.getQueueUrl(QUEUE_NAME).getQueueUrl();

        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest().withQueueUrl(queueUrl).withWaitTimeSeconds(10).withMaxNumberOfMessages(10);
        // receive messages from the queue with long polling
        List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();

        // delete messages from the queue
        for (Message m : messages) {
            sqs.deleteMessage(queueUrl, m.getReceiptHandle());
            SurescriptMessage demoMessage = objectMapper.readValue(m.getBody(), SurescriptMessage.class);
            System.out.println(objectMapper.writeValueAsString(demoMessage));
        }
    }

    @Scheduled(fixedRate = 1000L)
    public void receiveMessages() throws JsonProcessingException {
       receiveMessage();
    }
}
