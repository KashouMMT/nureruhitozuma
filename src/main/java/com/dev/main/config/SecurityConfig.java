package com.dev.main.config;

import javax.sql.DataSource;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.dev.main.components.CustomAccessDeniedHandler;
import com.dev.main.components.CustomAuthenticationEntryPoint;
import com.dev.main.components.CustomAuthenticationFailureHandler;
import com.dev.main.components.CustomAuthenticationSuccessHandler;
import com.dev.main.components.CustomLogoutSuccessHandler;
import com.dev.main.repository.AppSecretRepository;
import com.dev.main.repository.UserRepository;
import com.dev.main.security.MyUserDetailsService;
import com.dev.main.utils.MyRememberMeConfigurer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    private final MyUserDetailsService myUserDetailsService;
    private final UserRepository userRepo;
    private final DataSource dataSource;
    private final AppSecretRepository appSecretRepo;
    
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    SecurityConfig(UserRepository userRepo, MyUserDetailsService myUserDetailsService,DataSource dataSource,AppSecretRepository appSecretRepo) {
    	this.userRepo = userRepo;
    	this.myUserDetailsService = myUserDetailsService;
    	this.dataSource = dataSource;
    	this.appSecretRepo = appSecretRepo;
    }

	@Bean
	SecurityFilterChain filter(HttpSecurity http,MyRememberMeConfigurer rememberMeConfigurer)throws Exception {
		logger.info("Initializing security filter chain.");
		http
			.csrf(Customizer.withDefaults())
			.cors(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.headers(headers -> headers
				.frameOptions(frame -> frame.sameOrigin()))
			.authorizeHttpRequests(auth -> {
			logger.info("Configuring public and secure endpoints");
				auth 
				.requestMatchers("/",
								 "/**", //Normally this shouldn't be here but I'm not in the mood to do things properly
								 "/files/**",
								 "/ws/**",
								 "/topic/**","/app/**",
								 "/change-lang",
								 "/favicon.png",
								 "/favicon.ico",
								 "/favicon.svg",
								 "/robots.txt","/sitemap.xml",
								 "/error","/error/**",
								 "/public/**","/public/assets/**","/public/assets/images/**",
									          "/public/frontend/**","/public/frontend/css/**","/public/frontend/js/**",
							     "/en/home","/en/home/**","/en/auth/**",
							     "/ja/home","/ja/home/**","/ja/auth/**"
							     //"/ko/home","/ko/home/**","/ko/auth/**",
							     //"/zh/home","/zh/home/**","/zh/auth/**"
							     )
					.permitAll()
				.requestMatchers(
						"/en/admin/**",
						"/ja/admin/**",
						//"/ko/admin/**",
						//"/zh/admin/**",
						"/actuator/**").hasAuthority("ROLE_ADMIN")
				.requestMatchers(
						"/en/user/**",
						"/ja/user/**"
						//"/ko/user/**",
						//"/zh/user/**"
						).hasAuthority("ROLE_USER")
				.anyRequest().authenticated();
			})		
			.formLogin(form -> {
			logger.info("Custom login form setup. Login Page: [/{lang}/auth/login], Login Success URL: [/auth/sign-in]");
				form
				.loginPage("/{lang}/auth/login")
				.loginProcessingUrl("/auth/sign-in")
				.successHandler(authenticationSuccessHandler())
				.failureHandler(authenticationFailureHandler())
				.permitAll();
			})
			.logout(logout -> {
			logger.info("Logout configuration applied. Logout processing URL: /auth/logout");
				logout
				.logoutUrl("/auth/logout")
				.logoutSuccessHandler(logoutSuccessHandler())
				.clearAuthentication(true)
				.invalidateHttpSession(true)
				.permitAll();
			})
			.sessionManagement(session -> {
			logger.info("Session management configured: max -1 sessions");
				session
				.maximumSessions(-1);
			})
			.exceptionHandling(exceptionHandling -> {
			logger.info("Setting up exception handlers.");
				exceptionHandling
				.accessDeniedHandler(accessDeniedHandler())
				.authenticationEntryPoint(authenticationEntryPoint());
				
			});
		logger.info("Configuring Remember-Me functionality");
		rememberMeConfigurer.configure(http);
		return http.build();
	}
	
	@Bean
	MyUserDetailsService userDetailsService() {
		return new MyUserDetailsService(userRepo);
	}

    @Bean
    PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
	
    @Bean
    AuthenticationProvider authenticationProvider() {
    	DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService());
    	provider.setPasswordEncoder(passwordEncoder());
    	return provider;
    }
	
	@Bean
	AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new CustomAuthenticationSuccessHandler();
	}
	
	@Bean
	AuthenticationFailureHandler authenticationFailureHandler() {
		return new CustomAuthenticationFailureHandler();
	}
	
	@Bean
	AccessDeniedHandler accessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}
	
	@Bean
	AuthenticationEntryPoint authenticationEntryPoint() {
		return new CustomAuthenticationEntryPoint();
	}
	
	@Bean
	LogoutSuccessHandler logoutSuccessHandler() {
		return new CustomLogoutSuccessHandler();
	}
	
	@Bean
	PersistentTokenRepository persistentTokenRepository(Environment env) {
	    return MyRememberMeConfigurer.createTokenRepository(dataSource);
	}

    @Bean
    @DependsOnDatabaseInitialization
    MyRememberMeConfigurer myRememberMeConfigurer(PersistentTokenRepository tokenRepo) {
	    return new MyRememberMeConfigurer(
	        myUserDetailsService,
	        tokenRepo,
	        appSecretRepo
	    );
	}
}
