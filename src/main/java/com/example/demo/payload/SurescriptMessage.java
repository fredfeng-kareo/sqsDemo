package com.example.demo.payload;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class SurescriptMessage {
    public ErxMessageType requestType; // newRx, refill etc
    public String xmlContent;
    public Date date;
    public int retryCounter;
    public String messageId;

    public SurescriptMessage(ErxMessageType erxMessageType, String xmlContent, String messageId){
        this.requestType = erxMessageType;
        this.xmlContent = xmlContent;
        date = new Date();
        retryCounter = 0;
        this.messageId = messageId;
    }
}
