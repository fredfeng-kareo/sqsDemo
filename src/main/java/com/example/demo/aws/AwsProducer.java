package com.example.demo.aws;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.example.demo.payload.ErxMessageType;
import com.example.demo.payload.SurescriptMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.UUID;

@Component
public class AwsProducer {
    @Value("${queue.stand}")
    String QUEUE_NAME;

    @Autowired
    public AmazonSQSClientBuilder amazonSQSClientBuilder;

    @Autowired
    ObjectMapper objectMapper;

    public void publishMessage() throws IOException, XPathExpressionException, SAXException, ParserConfigurationException {
        final AmazonSQS sqs =  amazonSQSClientBuilder.build();

        String xmlMessage = MessageUtil.readFromFIle();
        String messageId = MessageUtil.getMessageId();

        SurescriptMessage surescriptMessage = new SurescriptMessage(
                ErxMessageType.NewRxRequest,
                xmlMessage ,
                messageId);

        String queueUrl = sqs.getQueueUrl(QUEUE_NAME).getQueueUrl();

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(objectMapper.writeValueAsString(surescriptMessage))
                .withDelaySeconds(5);

        sqs.sendMessage(send_msg_request);
    }

       private int total = 1;
    @Scheduled(fixedRate = 1000L)
    public void sendMessage() throws IOException, ParserConfigurationException, XPathExpressionException, SAXException {
        if(total > 20) {
            return;
        }
        publishMessage();
    }

}
