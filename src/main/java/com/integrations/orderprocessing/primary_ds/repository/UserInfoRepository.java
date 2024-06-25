package com.integrations.orderprocessing.primary_ds.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.integrations.orderprocessing.primary_ds.entity.UserInfo;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
   public Optional<UserInfo> findByUsername(String username);
}
