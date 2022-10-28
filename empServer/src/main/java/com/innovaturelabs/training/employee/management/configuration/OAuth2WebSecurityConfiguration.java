// package com.innovaturelabs.training.employee.management.configuration;

// import static org.springframework.http.HttpMethod.OPTIONS;
// import static org.springframework.http.HttpMethod.POST;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.core.annotation.Order;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

// @Configuration
// @Order(1)
// public class OAuth2WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

//     @Override
//     protected void configure(HttpSecurity http) throws Exception {

//         http.antMatcher("/oauth2/**").authorizeRequests()
//                 // .antMatchers(OPTIONS, "/**").permitAll()
//                 .anyRequest()
//                 .authenticated().and().oauth2Login();

//     }

// }
