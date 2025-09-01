package com.zotriverse.demo.repository;

import com.zotriverse.demo.pojo.Account;
import com.zotriverse.demo.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}