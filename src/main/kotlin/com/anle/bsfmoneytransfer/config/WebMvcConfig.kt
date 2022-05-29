package com.anle.bsfmoneytransfer.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.view.InternalResourceViewResolver


@Configuration
@EnableWebMvc
class WebMvcConfig(@Qualifier("dispatcherServletRegistration") val dispatcherServlet: ServletRegistrationBean<*>) : WebMvcConfigurer {

    @Bean
    fun logHttpRequestsFilterBean(): FilterRegistrationBean<*> {
        val bean = FilterRegistrationBean<LogFilter>()
        bean.filter = LogFilter()
        bean.addServletRegistrationBeans(dispatcherServlet)
        bean.order = 1

        return bean
    }
}