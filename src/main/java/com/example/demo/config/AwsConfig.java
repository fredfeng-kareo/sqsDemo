package com.example.demo.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class AwsConfig {
    public final static Logger LOGGER = LoggerFactory.getLogger(JmsConfig.class);

    @PostConstruct
    public void init() {
        LOGGER.info("Started init");
    }

    @Bean
    public AmazonSQSClientBuilder AmazonSQSClientBuilder(@Value("${cloud.aws.credentials.accessKey}") String awsAccessKey,
                                                         @Value("${cloud.aws.credentials.secretKey}") String awsSecretKey,
                                                         @Value("${cloud.aws.region.static}") String awsRegion,
                                                         @Value("${cloud.aws.endpoint.static}") String endPoint){
        return AmazonSQSClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAccessKey, awsSecretKey)))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, awsRegion));
    }

}
