package com.sxs.reggie.config;

import com.sxs.reggie.common.JacksonObjectMapper;
import com.sxs.reggie.interceptor.LoginCheckInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * @author sxs
 * @create 2022-08-19 14:19
 */
@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

/*    @Autowired
    private LoginCheckInterceptor loginCheckInterceptor;*/

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("加载静态资源映射。。。");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器");
        //创建消息转换器对象
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器，底层使用的是jackson
        mappingJackson2HttpMessageConverter.setObjectMapper(new JacksonObjectMapper());
        //添加到mvc框架的转换器集合中 0是优先级
        converters.add(0,mappingJackson2HttpMessageConverter);
    }
    /*
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("加载拦截器。。。");
        registry.addInterceptor(loginCheckInterceptor).addPathPatterns("/**");
    }
    */
}
