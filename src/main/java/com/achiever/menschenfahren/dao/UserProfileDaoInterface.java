package com.achiever.menschenfahren.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.achiever.menschenfahren.entities.users.User;
import com.achiever.menschenfahren.entities.users.UserProfile;

@Repository
public interface UserProfileDaoInterface extends JpaRepository<UserProfile, String> {

    List<UserProfile> findByVoided(final boolean alsoVoided);

    List<UserProfile> findByUser(User user);

}
