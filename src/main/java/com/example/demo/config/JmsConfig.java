package com.example.demo.config;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.example.demo.jms.SampleJmsErrorHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import javax.annotation.PostConstruct;
import javax.jms.Session;

@Configuration
@EnableJms
public class JmsConfig {

    public final static Logger LOGGER = LoggerFactory.getLogger(JmsConfig.class);

    // sqs client builder
    private final AmazonSQSClientBuilder SQSClientBuilder;
    private final SQSConnectionFactory connectionFactory;

    @PostConstruct
    public void init() {
        LOGGER.info("Started init");
    }

    public JmsConfig(
            @Value("${cloud.aws.credentials.accessKey}") String awsAccessKey,
            @Value("${cloud.aws.credentials.secretKey}") String awsSecretKey,
            @Value("${cloud.aws.region.static}") String awsRegion,
            @Value("${cloud.aws.endpoint.static}") String endPoint) {
        SQSClientBuilder = AmazonSQSClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAccessKey, awsSecretKey)))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, awsRegion));
        connectionFactory = new SQSConnectionFactory(new ProviderConfiguration(),this.SQSClientBuilder);
    }


    @Bean
    public JmsTemplate jmsTemplate(){
        JmsTemplate jmsTemplate = new JmsTemplate(this.connectionFactory);
        jmsTemplate.setMessageConverter(messageConverter());
        return jmsTemplate;
    }

    /**
     * Format
     * @return
     */
    @Bean
    public MessageConverter messageConverter(){
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.serializationInclusion(JsonInclude.Include.NON_EMPTY);
        builder.dateFormat(new ISO8601DateFormat());
        org.springframework.jms.support.converter.MappingJackson2MessageConverter mappingJackson2MessageConverter = new MappingJackson2MessageConverter();
        mappingJackson2MessageConverter.setObjectMapper(builder.build());
        mappingJackson2MessageConverter.setTargetType(MessageType.TEXT);
        mappingJackson2MessageConverter.setTypeIdPropertyName("documentType");
        return mappingJackson2MessageConverter;
    }


    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(){
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();

        factory.setConnectionFactory(this.connectionFactory);
        factory.setDestinationResolver(new DynamicDestinationResolver());

        //The values we provided to Concurrency show that we will create a minimum of 3 listeners that will scale up to 10 listeners
        factory.setConcurrency("3-10");

        factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);

        factory.setMessageConverter(messageConverter());
        factory.setErrorHandler(new SampleJmsErrorHandler());

        return factory;
    }
}
