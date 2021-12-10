package com.shf.securitydemo01.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

import static org.springframework.security.config.web.server.ServerHttpSecurity.http;

@Configuration
public class MySecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

//    注入数据源
    @Autowired
    private DataSource dataSource;

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
//        jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        退出
        http.logout().logoutUrl("/logout")
                .logoutSuccessUrl("/test/hello").permitAll();

//        配置没有访问权限跳转自定义页面
        http.exceptionHandling().accessDeniedPage("/unauth.html");

        http.formLogin()  // 自定义自己编写的页面
                .loginPage("/login.html")  // 登录页面设置
                .loginProcessingUrl("/user/login")  // 登录访问路径
//                .defaultSuccessUrl("/test/index").permitAll()  // 登录成功之后，跳转路径;
                .defaultSuccessUrl("/success.html").permitAll()  // 登录成功之后，跳转路径;
                .and().authorizeRequests()
                    .antMatchers("/","/test/hello","/user/login").permitAll()  // 设置哪些路径可以直接访问，不需要认证
//                .antMatchers("/test/index").hasAuthority("admins")
//                .antMatchers("/test/index").hasAnyAuthority("admins")
//                .antMatchers("/test/index").hasRole("sale")
                .antMatchers("/test/index").hasAnyRole("sale")
                .anyRequest().authenticated()

                .and().rememberMe().tokenRepository(persistentTokenRepository()) // 记住我
                .tokenValiditySeconds(60)  // 设置有效时长，单位秒
                .userDetailsService(userDetailsService)
                .and().csrf().disable(); // 关闭csrf防护
    }
}
