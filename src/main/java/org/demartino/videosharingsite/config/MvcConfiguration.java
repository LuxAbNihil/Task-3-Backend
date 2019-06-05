package org.demartino.videosharingsite.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ComponentScan(basePackages="org.demartino.videosharingsite")
@EnableWebMvc
public class MvcConfiguration extends WebMvcConfigurerAdapter{

}
