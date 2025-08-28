//package com.rookies4.myspringbootlab.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/mobile/**")
//                //반드시 mobile 다음에 / 을 주어야 한다.
//                .addResourceLocations("classpath:/mobile/")
//                .setCachePeriod(20);//20초
//    }
//    @Override //다른 로컬호스트(client)가 백엔드서버와 Resource를 연결하기 위한 코드
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowedMethods("*");;
//    }
//}