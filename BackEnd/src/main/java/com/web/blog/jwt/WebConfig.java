package com.web.blog.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);
	
	private final String uploadImagesPath;
	
	public WebConfig(@Value("${custom.path.upload-images}") String uploadImagesPath) {
		this.uploadImagesPath = uploadImagesPath;
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/images/**")
		//.addResourceLocations("file:///C:/images/");
	    .addResourceLocations("file:///home/ubuntu/images/");
	}

	private static final String[] EXCLUDE_PATHS = {
			"/account/**",
			"/post/search/**",
			"/thumbnails/**",
			"/profiles/**",
			"/tier/**",
			"/mappers/**",
			"/user/rank/**",
			"/error/**",
			"/post/latest/**",
			"/post/toppost/**",
			"/comment/get/**",
			"/post/detail/**",
			"/csrf/**",
			"/v2/api-docs/**",
            "/swagger-resources/**",
            "/swagger-ui.html/**",
            "/webjars/**",
            "/post/template/**",
            "/post/tags/**",
            "/images/**",
            "/post/other/**",
            "/SSAFY/images/**",
            "/user/bloguser/**"
	};
	
	@Autowired
	private JwtInterceptor jwtInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(jwtInterceptor)
										.addPathPatterns("/**")
										.excludePathPatterns(EXCLUDE_PATHS);
	}	
}