package com.example.demo.config;

//通过配置类设置账号密码

import com.example.demo.Service.UserService;
import com.example.demo.Service.UserServiceIml;
import com.example.demo.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class SecurityConfig implements AuthenticationProvider {

    PasswordEncoder passwordEncoder;
    @Autowired
    void setPasswordEncoder(PasswordEncoder passwordEncoder){
        this.passwordEncoder=passwordEncoder;
    }
    UserServiceIml userService;
    @Autowired
    void setUserService(UserServiceIml userService){
        this.userService=userService;
    }


    private final Logger logger= LoggerFactory.getLogger(getClass());
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        String username=token.getName();
        String password= token.getCredentials().toString();
        logger.info("用户名："+username);
        logger.info("密 码："+password);
        UserDetails userDetails = userService.loadUserByUsername(username);
        if(userDetails==null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        boolean flag=passwordEncoder.matches(password,userDetails.getPassword());
        if(!flag) {
            throw new UsernameNotFoundException("密码错误");
        }
        logger.info(username+"的权限是"+ userDetails.getAuthorities());
        return new UsernamePasswordAuthenticationToken(username,password,userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }
}

