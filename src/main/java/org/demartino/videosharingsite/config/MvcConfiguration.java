package org.demartino.videosharingsite.config;

import org.demartino.videosharingsite.httpinterceptor.AuthenticationInterceptor;
//import org.demartino.videosharingsite.httpinterceptor.AuthenticationInterceptor;
import org.demartino.videosharingsite.jwt.JwtOperations;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ComponentScan(basePackages="org.demartino.videosharingsite")
@EnableWebMvc
public class MvcConfiguration extends WebMvcConfigurerAdapter {

	@Autowired
	private AuthenticationInterceptor authenticationInterceptor;
	
	public static final Logger logger = Logger.getLogger(MvcConfiguration.class);
	
    public void addCorsMappings(CorsRegistry registry) {
    	logger.debug("IN CORS METHOD");
    	registry.addMapping("/**")
        	.allowedMethods("POST", "DELETE", "GET", "PUT", "OPTIONS")
        	.allowedOrigins("http://localhost:4200");
    }
    //
    @Bean
    public JwtOperations createJwtOperationsBean() {
    	return new JwtOperations();
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry iRegistry) {
    	iRegistry.addInterceptor(authenticationInterceptor)
    		.excludePathPatterns("/user/login/", "/user/getUserAndVideoListContainer/**", 
    				"/video/**", "/user/forgotPassword/", "/user/resetPassword/");
    }
    
    @Bean
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(1000000000);
		return multipartResolver;
	}
    
    @Bean
    public PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    } 
}
