package com.innovaturelabs.training.employee.management.configuration;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.innovaturelabs.training.employee.management.security.AccessTokenProcessingFilter;
import com.innovaturelabs.training.employee.management.security.AccessTokenUserDetailsService;
import com.innovaturelabs.training.employee.management.security.config.SecurityConfig;
import com.innovaturelabs.training.employee.management.security.util.TokenGenerator;

@Configuration
@Order(2)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String ADMIN = "ADMIN";
    private static final String EMPLOYER = "EMPLOYER";
    private static final String EMPLOYEE = "EMPLOYEE";

    public WebSecurityConfiguration() {
        super(true);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .requestMatcher(new NegatedRequestMatcher(new AntPathRequestMatcher("/error")))
                .addFilter(accessTokenProcessingFilter())
                .authenticationProvider(preAuthenticatedAuthenticationProvider())
                .exceptionHandling().and()
                .headers().and()
                .sessionManagement().sessionCreationPolicy(STATELESS).and()
                .securityContext().and()
                .anonymous().and()
                .authorizeRequests()
                .antMatchers(OPTIONS, "/users").anonymous()
                .antMatchers(POST, "/users").anonymous()
                .antMatchers("/login").anonymous()
                .antMatchers("/job/lastJobs").anonymous()
                // .antMatchers("/login/oauth2/code/**").anonymous()
                .antMatchers("/oauth2/login").anonymous()
                .antMatchers("/oauth2/register").anonymous()
                .antMatchers("/oauth2/verify-email").anonymous()
                .antMatchers("/oauth2/user-test").anonymous()
                .antMatchers(PUT, "/login/forgot-password").anonymous()
                .antMatchers("/users/verify-user").anonymous()
                .antMatchers(PUT, "/login/reset-password/**").anonymous()
                .antMatchers(PUT, "/job/status").hasAnyRole(ADMIN, EMPLOYER)
                .antMatchers(GET, "/job/page/**").hasAnyRole(EMPLOYER, ADMIN, EMPLOYEE)
                .antMatchers(GET, "/users/page/**").hasAnyRole(ADMIN)
                .antMatchers(GET, "/users/role-stat").hasAnyRole(ADMIN)
                // .antMatchers(PUT, "/users/delete/**").hasAnyRole(ADMIN)
                .antMatchers(PUT, "/users/download").hasAnyRole(ADMIN)
                // .antMatchers(GET, "/job/**").hasRole(ADMIN)
                // .antMatchers(OPTIONS, "/**").permitAll()
                .anyRequest().authenticated();
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("*")
                        .allowedOrigins("*")
                        .allowCredentials(true);
            }
        };
    }

    @Bean
    protected AccessTokenUserDetailsService accessTokenUserDetailsService() {
        return new AccessTokenUserDetailsService();
    }

    @Bean
    protected PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider() {
        PreAuthenticatedAuthenticationProvider authProvider = new PreAuthenticatedAuthenticationProvider();
        authProvider.setPreAuthenticatedUserDetailsService(accessTokenUserDetailsService());
        return authProvider;
    }

    @Bean
    protected AccessTokenProcessingFilter accessTokenProcessingFilter() throws Exception {
        AccessTokenProcessingFilter filter = new AccessTokenProcessingFilter();
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    @ConfigurationProperties("app.security")
    public SecurityConfig securityConfig() {
        return new SecurityConfig();
    }

    @Bean
    @ConfigurationProperties("app.security.configuration")
    public TokenGenerator tokenGenerator(SecurityConfig securityConfig) {
        return new TokenGenerator(securityConfig.getTokenGeneratorPassword(), securityConfig.getTokenGeneratorSalt());
    }

}
