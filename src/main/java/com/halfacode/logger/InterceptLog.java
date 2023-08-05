package com.halfacode.logger;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Order(value = 1)
public class InterceptLog implements HandlerInterceptor {

    private final LoggingService loggingService;

    public InterceptLog(LoggingService loggingService) {
        this.loggingService = loggingService;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getMethod().equals(HttpMethod.GET.name())
                || request.getMethod().equals(HttpMethod.DELETE.name())
                || request.getMethod().equals(HttpMethod.PUT.name())
                || request.getMethod().equals(HttpMethod.POST.name())
        )    {
            loggingService.displayReq(request,null);
        }
        return true;
    }
}