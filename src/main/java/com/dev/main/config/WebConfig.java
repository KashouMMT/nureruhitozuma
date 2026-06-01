package com.dev.main.config;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggerConfiguration;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.dev.main.components.ModelLoggingInterceptor;
import com.dev.main.components.RequestLoggingInterceptor;
import com.dev.main.serviceImpl.FileStorageServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	
	private final FileStorageServiceImpl storage;
	private final RequestLoggingInterceptor requestLoggingInterceptor;
	private final ModelLoggingInterceptor modelLoggingInterceptor;
	private final LoggingSystem loggingSystem;

	public WebConfig(FileStorageServiceImpl storage, RequestLoggingInterceptor requestLoggingInterceptor,
			ModelLoggingInterceptor modelLoggingInterceptor, LoggingSystem loggingSystem) {
		super();
		this.storage = storage;
		this.requestLoggingInterceptor = requestLoggingInterceptor;
		this.modelLoggingInterceptor = modelLoggingInterceptor;
		this.loggingSystem = loggingSystem;
	}

    @Bean
    LocaleResolver localeResolver() {
  		return new LocaleResolver() {
  		    private static final List<Locale> SUPPORTED_LOCALES = List.of(
  		            //Locale.ENGLISH,
  		            //Locale.CHINESE,
  		            //Locale.KOREAN
  		    		Locale.JAPANESE
  		    );
  		    
  		    private static final Locale DEFAULT_LOCALE = Locale.JAPANESE;
			
			@Override
			public Locale resolveLocale(HttpServletRequest request) {
				String uri = request.getRequestURI();
                String[] parts = uri.split("/");       

                if (parts.length > 1) {
                    String lang = parts[1];            
                    for (Locale locale : SUPPORTED_LOCALES) {
                        if (locale.getLanguage().equalsIgnoreCase(lang)) {
                            return locale;
                        }
                    }
                }

                return DEFAULT_LOCALE;
			}
			
			@Override
			public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {}
		};
  	}
  	
  	@Override
  	public void addInterceptors(InterceptorRegistry registry) {
  		
  		LogLevel level = resolveEffectiveRootLevel();
  		
  		if(level == LogLevel.DEBUG) registry.addInterceptor(requestLoggingInterceptor).addPathPatterns("/**");
  		else if (level == LogLevel.INFO) registry.addInterceptor(requestLoggingInterceptor)
  		.addPathPatterns("/en/home/**","/ja/home/**","/ko/home/**","/zh/home/**",
  						 "/en/admin/**","/ja/admin/**","/ko/admin/**","/zh/admin/**",
  						 "/en/auth/**","/ja/auth/**","/ko/auth/**","/zh/auth/**",
  						 "/en/user/**","/ja/user/**","/ko/user/**","/zh/user/**",
  						 "/robots.txt","/sitemap.xml");
  		//registry.addInterceptor(localChangeInterceptor());
  		registry.addInterceptor(modelLoggingInterceptor).addPathPatterns("/**");
  	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
		String location = storage.getRoot().toAbsolutePath().toUri().toString();
		registry.addResourceHandler("/files/**")
				.addResourceLocations(location)
				.setCachePeriod(3600);
	}
	
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.defaultContentType(MediaType.ALL);
	}
	
	private LogLevel resolveEffectiveRootLevel() {
		LoggerConfiguration rootConfig = loggingSystem.getLoggerConfiguration("ROOT");
	    if (rootConfig == null) {
	        rootConfig = loggingSystem.getLoggerConfiguration("root");
	    }
	    if(rootConfig != null && rootConfig.getEffectiveLevel() != null) {
	    	return rootConfig.getEffectiveLevel();
	    }
	    return loggingSystem.getLoggerConfigurations().stream()
	            .filter(cfg -> cfg.getName() == null || cfg.getName().isBlank() || "ROOT".equalsIgnoreCase(cfg.getName()))
	            .map(LoggerConfiguration::getEffectiveLevel)
	            .filter(Objects::nonNull)
	            .findFirst()
	            .orElse(LogLevel.INFO); // sensible default
	}
}
