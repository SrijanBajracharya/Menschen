package com.achiever.menschenfahren.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.achiever.menschenfahren.entities.roles.Role;

@Repository
public interface RoleDaoInterface extends JpaRepository<Role, String> {

}
