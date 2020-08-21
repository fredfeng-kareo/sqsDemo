package com.example.demo.payload;

public enum ErxMessageType {
    NewRxRequest,
    NewRxRequestReply,
    ChangeRequest,
    ChangeRequestResponse,
    ChangeRequestResponseReply,
    RefillRequest,
    RefillRequestResponse,
    RefillRequestResponseReply,
    CancelRequest,
    CancelRequestReply,
    FillRxNotification,
    SendFailureNotification,
    RxHistoryRequest,
    RxHistoryRequestResponse
}
