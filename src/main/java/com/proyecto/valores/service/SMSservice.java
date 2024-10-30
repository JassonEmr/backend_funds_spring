package com.proyecto.valores.service;

import org.springframework.stereotype.Service;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@Service
public class  SMSservice{

    private final SnsClient snsClient;

    public SMSservice() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create("AKIAXWMA6XXW2OC44P2G",
                "rhuD+UyGXa91bpXEYtAE5l39thXoa6Cx+mOFJGth");
        this.snsClient = SnsClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .region(Region.US_EAST_2) // Cambia a tu región
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
