package com.zotriverse.demo.repository;

import com.zotriverse.demo.pojo.Admin;
import com.zotriverse.demo.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository  extends JpaRepository<Admin, Integer> {

}