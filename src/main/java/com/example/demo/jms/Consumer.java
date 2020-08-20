//package com.example.demo.jms;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//@Component
//public class Consumer {
//    private final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);
////
////    @JmsListener(destination = "${queue.test}")
////    public void listenQueueTest(@Payload final Message<DemoMessage> message) {
////        LOGGER.info("Listening {} in queue test", message.getPayload().content);
////    }
////
////    @JmsListener(destination = "${queue.fifo}")
////    public void listenQueueFifo(@Payload final Message<DemoMessage> message) {
////        LOGGER.info("Listening {} in queue fifo", message.getPayload().content);
////    }
//}