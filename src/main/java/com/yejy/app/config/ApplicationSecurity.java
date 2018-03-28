package com.yejy.app.config;

import com.yejy.app.filter.AuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class ApplicationSecurity  extends WebSecurityConfigurerAdapter {
    @Autowired
    private AuthenticationProvider provider;  //注入我们自己的AuthenticationProvider

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(provider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().loginPage("/login.html")
            .loginProcessingUrl("/login/form")
            .failureUrl("/login-error.html").permitAll()  //表单登录，permitAll()表示这个不需要验证 登录页面，登录失败页面
            .and()
            .authorizeRequests().anyRequest().authenticated()
            .and()
            .csrf()
            .disable()
            .sessionManagement() // 定制我们自己的 session 策略
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 调整为让 Spring Security 不创建和使用 session

        http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public AuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        AuthenticationTokenFilter authenticationTokenFilter = new AuthenticationTokenFilter();
        authenticationTokenFilter.setAuthenticationManager(authenticationManagerBean());
        return authenticationTokenFilter;
    }

}
