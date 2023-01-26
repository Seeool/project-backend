package com.example.projectbackend.config;

import com.example.projectbackend.security.MemberDetailsService;
import com.example.projectbackend.security.filter.NormalLoginFilter;
import com.example.projectbackend.security.handler.NormalLoginFailureHandler;
import com.example.projectbackend.security.handler.NormalLoginSuccessHandler;
import com.example.projectbackend.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final MemberDetailsService memberDetailsService;
    private final PasswordEncoderConfig passwordEncoderConfig;
    private final JWTUtil jwtUtil;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //AuthenticationManager
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(memberDetailsService).passwordEncoder(passwordEncoderConfig.passwordEncoder());
        AuthenticationManager authenticationManager = builder.build();
        http.authenticationManager(authenticationManager);

        //NormalLoginFilter
        NormalLoginFilter normalLoginFilter = new NormalLoginFilter("/loginProc", authenticationManager);
        NormalLoginSuccessHandler normalLoginSuccessHandler = new NormalLoginSuccessHandler(jwtUtil);
        NormalLoginFailureHandler normalLoginFailureHandler = new NormalLoginFailureHandler();
        SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
        normalLoginFilter.setAuthenticationSuccessHandler(normalLoginSuccessHandler);
        normalLoginFilter.setAuthenticationFailureHandler(normalLoginFailureHandler);
        normalLoginFilter.setSecurityContextRepository(securityContextRepository);
        http.addFilterBefore(normalLoginFilter, UsernamePasswordAuthenticationFilter.class);

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeHttpRequests().requestMatchers("/**").permitAll();
        http.cors(httpSecurityCorsConfigurer -> corsConfigurationSource());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD","GET","POST","PUT","DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization","Cache-Control","Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
