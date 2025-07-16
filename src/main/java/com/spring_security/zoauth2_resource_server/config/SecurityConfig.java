package com.spring_security.zoauth2_resource_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	//공개키로 디코딩하는 검증 작업을 수행
    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation("http://localhost:9000");
    }
    
    @Bean
    public SecurityFilterChain resourceServer(HttpSecurity http) throws Exception {

        http.oauth2ResourceServer((resource) -> resource.jwt(Customizer.withDefaults()));

        http.authorizeHttpRequests(
    		(auth) -> auth.requestMatchers("/me").hasAuthority("SCOPE_profile")
                          .requestMatchers("/admin").hasAnyAuthority("SCOPE_admin", "ROLE_ADMIN")
                          .anyRequest().authenticated()
        );

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
	
}
