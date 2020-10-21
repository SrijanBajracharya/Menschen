package com.achiever.menschenfahren.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.achiever.menschenfahren.entities.common.Country;

@Repository
public interface CountryDaoInterface extends JpaRepository<Country, String> {

}
