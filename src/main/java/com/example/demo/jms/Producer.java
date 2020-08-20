//package com.example.demo.jms;
//
//import com.amazon.sqs.javamessaging.SQSMessagingClientConstants;
//import com.example.demo.payload.DemoMessage;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.jms.core.JmsTemplate;
//import org.springframework.jms.core.MessageCreator;
//import org.springframework.stereotype.Component;
//
//import javax.jms.JMSException;
//import javax.jms.Message;
//import javax.jms.Session;
//import java.io.Serializable;
//
///**
// * Producer
// */
//@Component
//public class Producer {
//    private final Logger LOGGER = LoggerFactory.getLogger(Producer.class);
//    @Autowired
//    protected JmsTemplate jmsTemplate;
//
//    @Value("${queue.test}")
//    String testQueue;
//
//    @Value("${queue.fifo}")
//    String fifoqueue;
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//    public void sendToTestQueue(DemoMessage message){
//        LOGGER.info("Sending message {} to queue {}", message, "fifoQueue");
//        send(testQueue, message);
//    }
//
//    /**
//     * send messages
//     * @param queue
//     * @param payload
//     * @param <MESSAGE>
//     */
//    public <MESSAGE extends Serializable> void send(String queue, MESSAGE payload) {
//        jmsTemplate.send(queue, new MessageCreator() {
//            @Override
//            public Message createMessage(Session session) throws JMSException {
//                try {
//                    Message createMessage = session.createTextMessage(objectMapper.writeValueAsString(payload));
//                    createMessage.setStringProperty(SQSMessagingClientConstants.JMSX_GROUP_ID, "messageGroup1");
//                    createMessage.setStringProperty(SQSMessagingClientConstants.JMS_SQS_DEDUPLICATION_ID, "2019" + System.currentTimeMillis());
//                    createMessage.setStringProperty("documentType", payload.getClass().getName());
//
//                    return createMessage;
//                } catch (JsonProcessingException e) {
//                    e.printStackTrace();
//                    LOGGER.error("Fail to send message {} ,err {}", payload, e.getMessage());
//                    throw new RuntimeException(e);
//                }
//            }
//        });
//    }
//
////    private int total = 1;
////    @Scheduled(fixedRate = 1000L)
////    public void sendMessage(){
////        if(total > 3 ){
////            return;
////
////        }
////        DemoMessage demoMessage = new DemoMessage(UUID.randomUUID().toString(),"Test queue " + total++,new Date());
////        sendToTestQueue(demoMessage);
////    }
//
////    @Scheduled(fixedRate = 2000L)
////    public void sendMessageFifo(){
////        DemoMessage demoMessage = new DemoMessage(UUID.randomUUID().toString(),"fifo queue " + total++,new Date());
////        LOGGER.info("Sending message {} to queue {}", demoMessage, "fifoQueue");
////        send(fifoqueue, demoMessage);
////    }
//}