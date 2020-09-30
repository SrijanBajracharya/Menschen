package com.achiever.menschenfahren.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.achiever.menschenfahren.entities.users.UserProfile;

@Repository
public interface UserProfileDaoInterface extends JpaRepository<UserProfile, String> {

}
