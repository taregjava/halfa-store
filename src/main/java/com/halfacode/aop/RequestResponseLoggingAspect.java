package com.halfacode.aop;

import com.halfacode.logger.LoggingContent;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

//@Aspect
//@Component
public class RequestResponseLoggingAspect {

    /*private final S3Service s3Service;

    @Autowired
    public RequestResponseLoggingAspect(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @Pointcut("execution(public * com.halfacode.service.*.*(..)) || " +
            "execution(public * com.halfacode.controller.*.*(..))")
    public void methodsToLog() {}

    @AfterReturning(pointcut = "methodsToLog()", returning = "result")
    public void logRequestAndResponse(ResponseEntity<?> result) {
        // Extract request details using HttpServletRequest
        // Extract response details from ResponseEntity

        // Save request and response data to S3
        String logData = buildLogData(requestDetails, responseDetails);
        s3Service.uploadLog("request-response-logs", logData);
    }

    private String buildLogData(String requestDetails, String responseDetails) {
        // Build a formatted log entry
        // You can include timestamps, headers, payloads, etc.
        return "Request: " + requestDetails + "\nResponse: " + responseDetails;
    }
}*/
}