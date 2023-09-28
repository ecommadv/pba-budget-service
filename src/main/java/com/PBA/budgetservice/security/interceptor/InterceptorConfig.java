package com.PBA.budgetservice.security.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class InterceptorConfig implements WebMvcConfigurer {
    private static final String EXPENSE_CATEGORY_ENDPOINT = "/expense/category";
    private static final String INCOME_CATEGORY_ENDPOINT = "/income/category";
    private static final String[] EXCLUDED_ENDPOINTS = {EXPENSE_CATEGORY_ENDPOINT, INCOME_CATEGORY_ENDPOINT};
    private final JwtHeaderHandlerInterceptor jwtHeaderHandlerInterceptor;

    public InterceptorConfig(JwtHeaderHandlerInterceptor jwtHeaderHandlerInterceptor) {
        this.jwtHeaderHandlerInterceptor = jwtHeaderHandlerInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtHeaderHandlerInterceptor)
                .excludePathPatterns(EXCLUDED_ENDPOINTS);
    }
}
