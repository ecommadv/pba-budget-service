package com.PBA.budgetservice.security.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class InterceptorConfig implements WebMvcConfigurer {
    private static final String EXPENSE_CATEGORY_ENDPOINT = "/expense/category";
    private static final String INCOME_CATEGORY_ENDPOINT = "/income/category";
    private final JwtValidationInterceptor jwtValidationInterceptor;

    public InterceptorConfig(JwtValidationInterceptor jwtValidationInterceptor) {
        this.jwtValidationInterceptor = jwtValidationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtValidationInterceptor)
                .excludePathPatterns(EXPENSE_CATEGORY_ENDPOINT, INCOME_CATEGORY_ENDPOINT);
    }
}
