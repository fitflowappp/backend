package com.magpie.mvcconfig;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.magpie.session.ActiveUserWebArgumentResolver;

@EnableWebMvc
@Configuration
public class SpringMvcConfiguration extends WebMvcConfigurerAdapter {
	//
	// @Autowired
	// private ActDynamicConfig actDynamicConfig;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/js/**").addResourceLocations("/WEB-INF/js/");

		registry.addResourceHandler("/css/**").addResourceLocations("/WEB-INF/css/");

		registry.addResourceHandler("/img/**").addResourceLocations("/WEB-INF/img/");

		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

	}

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver("/WEB-INF/jsp/", ".jsp");
		registry.viewResolver(viewResolver);
	}

	// @Override
	// public void addInterceptors(InterceptorRegistry registry) {
	// CSRFInterceptor csrfInterceptor = new CSRFInterceptor("", "");
	// InterceptorRegistration interceptorRegistration =
	// registry.addInterceptor(csrfInterceptor);
	// interceptorRegistration.addPathPatterns("/**");
	// interceptorRegistration.excludePathPatterns("/test/**");
	//
	// }

	// @Override
	// public void configureMessageConverters(List<HttpMessageConverter<?>>
	// converters) {
	// converters.add(new AdvanceFastJsonHttpMessageConverter());
	// }

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new ActiveUserWebArgumentResolver());
	}
}
