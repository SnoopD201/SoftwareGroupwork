package com.example.trainserve.entiy;

import lombok.Data;

@Data
public class Alipay {
    private String traceNo;
    private double totalAmount;
    private String subject;
    private String alipayTraceNo;
}
