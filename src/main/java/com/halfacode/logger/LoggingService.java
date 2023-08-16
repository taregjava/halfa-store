package com.halfacode.logger;


import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface LoggingService {

    void displayReq(HttpServletRequest request, Object body) throws JsonProcessingException;

    void displayResp(HttpServletRequest request, HttpServletResponse response, Object body) throws JsonProcessingException;
}