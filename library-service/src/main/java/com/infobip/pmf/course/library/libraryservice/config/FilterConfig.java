package com.infobip.pmf.course.library.libraryservice.config;

import com.infobip.pmf.course.library.libraryservice.api.ApiKeyAuthFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<ApiKeyAuthFilter> apiKeyAuthFilter() {
        FilterRegistrationBean<ApiKeyAuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ApiKeyAuthFilter());
        registrationBean.addUrlPatterns("/libraries/*", "/libraries/*/versions/*");
        return registrationBean;
    }
}

