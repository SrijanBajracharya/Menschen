package com.achiever.menschenfahren.dao;

import java.util.Optional;

import javax.annotation.Nonnull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.achiever.menschenfahren.entities.users.Avatar;

@Repository
public interface AvatarDaoInterface extends JpaRepository<Avatar, String> {

    Optional<Avatar> findByUserId(@Nonnull final String userId);

}
