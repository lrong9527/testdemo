package com.example.demo.Service;

import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceIml implements UserService{

    private final UserMapper userMapper;
    @Autowired
    public UserServiceIml(UserMapper userMapper){
        this.userMapper=userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user= userMapper.selectByPrimaryKey(username);
        if(user==null){
            throw new UsernameNotFoundException("用户不存在");
        }
        return user;
    }

}
