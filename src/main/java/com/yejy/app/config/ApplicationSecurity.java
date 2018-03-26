package com.yejy.app.config;

import com.yejy.app.security.PzPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class ApplicationSecurity  extends WebSecurityConfigurerAdapter {
    @Autowired
    private AuthenticationProvider provider;  //注入我们自己的AuthenticationProvider

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(provider);
//        auth.inMemoryAuthentication().passwordEncoder(new PzPasswordEncoder())
//                .withUser("user").password("user123").roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

//        http.authorizeRequests()
////                .anyRequest().permitAll();
//                .antMatchers("/static/**").permitAll()
//                .anyRequest().fullyAuthenticated().and().formLogin().loginPage("/login.html").loginProcessingUrl("/login/form")
//                .failureUrl("/login?error").permitAll().and().logout().permitAll();
        http
                .formLogin().loginPage("/login.html")
                .loginProcessingUrl("/login/form")
                .failureUrl("/login-error.html").permitAll()  //表单登录，permitAll()表示这个不需要验证 登录页面，登录失败页面
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .csrf().disable();
    }

}
