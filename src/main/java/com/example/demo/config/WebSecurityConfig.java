package com.example.demo.config;

import com.example.demo.Service.UserServiceIml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    UserServiceIml userService;
    @Autowired
    void setUserService(UserServiceIml userService){
        this.userService=userService;
    }

    SecurityConfig securityConfig;
    @Autowired
    void setSecurityConfig(SecurityConfig securityConfig){
        this.securityConfig=securityConfig;
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        设置用户名 、角色 查询逻辑
        auth.userDetailsService(userService);
//        设置自定义校验逻辑
        auth.authenticationProvider(securityConfig);
    }

    /**
     * 静态资源放行配置
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/css/**").antMatchers("/image/**");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .mvcMatchers("/login").permitAll()
                .mvcMatchers("/index.html").permitAll() //放行index.Html请求
                .anyRequest().authenticated()//代表其他请求需要认证
                .and()
                .formLogin()//表示其他需要认证的请求通过表单认证
                //loginPage 一旦你自定义了这个登录页面，那你必须要明确告诉SpringSecurity日后哪个url处理你的登录请求
                .loginPage("/index.html")//用来指定自定义登录界面，不使用SpringSecurity默认登录界面  注意：一旦自定义登录页面，必须指定登录url
                //loginProcessingUrl  这个doLogin请求本身是没有的，因为我们只需要明确告诉SpringSecurity，日后只要前端发起的是一个doLogin这样的请求，
                .loginProcessingUrl("/login")//指定处理登录的请求url
//                .successForwardUrl("/index")//认证成功 forward 跳转路径，forward代表服务器内部的跳转之后，地址栏不变 始终在认证成功之后跳转到指定请求
//                .defaultSuccessUrl("/success")//认证成功 之后跳转，重定向 redirect 跳转后，地址会发生改变  根据上一保存请求进行成功跳转
                .and()
                .csrf().disable(); //禁止csrf 跨站请求保护
    }
}
