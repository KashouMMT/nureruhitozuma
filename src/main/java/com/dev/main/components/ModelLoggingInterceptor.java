package com.dev.main.components;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ModelLoggingInterceptor implements HandlerInterceptor{
	
	private static final Logger logger = LoggerFactory.getLogger(ModelLoggingInterceptor.class);
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,ModelAndView modelAndView) {
		
		if(modelAndView == null) {
			return;
		}
		
		if (!logger.isDebugEnabled()) {
		    return;
		}
		
		Map<String,Object> model = modelAndView.getModel();
		if(model == null || model.isEmpty()) {
			return;
		}
		
		if(handler instanceof HandlerMethod handlerMethod) {
		    String controller = handlerMethod.getBeanType().getSimpleName();
		    String method = handlerMethod.getMethod().getName();
		    logger.info("Model for [{}] -> view='{}' ({}#{})",
		            request.getRequestURI(),
		            modelAndView.getViewName(),
		            controller,
		            method);
		} else {
		    logger.info("Model for [{}] -> view='{}'",
		            request.getRequestURI(),
		            modelAndView.getViewName());
		}
		
        model.forEach((key, value) -> {
        	
            // Avoid dumping huge objects / collections fully
            String valueSummary;
            if (value == null) {
                valueSummary = "null";
            } else if (value instanceof Collection<?> c) {
                valueSummary = "Collection(size=" + c.size() + ", type=" + value.getClass().getSimpleName() + ")";
            } else if (value instanceof Map<?,?> m) {
                valueSummary = "Map(size=" + m.size() + ", type=" + value.getClass().getSimpleName() + ")";
            } else {
                valueSummary = String.valueOf(value);
            }

            logger.debug("  [{}] = {}", key, valueSummary);
        });
	}
}
