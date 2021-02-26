package com.achiever.menschenfahren.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.achiever.menschenfahren.entities.feedback.Feedback;

@Repository
public interface FeedbackDaoInterface extends JpaRepository<Feedback, String> {

}
