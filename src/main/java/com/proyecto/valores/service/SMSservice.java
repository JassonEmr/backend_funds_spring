package com.proyecto.valores.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@Service
public class  SMSservice{

    private final SnsClient snsClient;

    public SMSservice(@Value("${aws.accessKeyId}") String accessKeyId,
                        @Value("${aws.secretKey}") String secretKey,
                        @Value("${aws.ses.region}") String region) {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId,secretKey);
        this.snsClient = SnsClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .region(Region.of(region)) // Cambia a tu región
                .build();
    }

    public void sendSms(String phoneNumber, String message){
        PublishRequest request = PublishRequest.builder()
        .message(message)
        .phoneNumber(phoneNumber)
        .build();

        snsClient.publish(request);
    }
}
