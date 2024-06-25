package com.integrations.orderprocessing.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.integrations.orderprocessing.configuration.CustomUserDetails;
import com.integrations.orderprocessing.primary_ds.entity.UserInfo;
import com.integrations.orderprocessing.primary_ds.repository.UserInfoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.debug("Entering in loadUserByUsername Method...");
        Optional<UserInfo> userInfo = userInfoRepository.findByUsername(username);
        if(!userInfo.isPresent()){
            log.error("Username not found: " + username);
            throw new UsernameNotFoundException("Could not found user..!!");
        }
        
        log.info("User Authenticated Successfully..!!!");
        return new CustomUserDetails(userInfo.get());
    }
}
