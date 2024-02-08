package com.karhabtiapp.configurations;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Filter;
import java.util.logging.LogRecord;



@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SimpleCorsFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException{
        HttpServletResponse response1= (HttpServletResponse) response;
        HttpServletRequest request1=(HttpServletRequest) request;
        Map<String,String> map = new HashMap<>();
        String originHeader= request1.getHeader("origin");
        response1.setHeader("Acces-Control-Allow-Origin", originHeader);
        response1.setHeader("Acces-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE ");
        response1.setHeader("Acces-Control-Max-Age", "36000");
        response1.setHeader("Acces-Control-Allow-Headers", "*");

        if("OPTIONS".equalsIgnoreCase(request1.getMethod())){

            response1.setStatus(HttpServletResponse.SC_OK);
        }else {
            filterChain.doFilter(request,response);
        }

    }


    public void init(FilterConfig filterConfig){
    }


    public void destroy(){

    }
    @Override
    public boolean isLoggable(LogRecord record) {
        // Loggable logic here if needed
        return true;
    }

}
