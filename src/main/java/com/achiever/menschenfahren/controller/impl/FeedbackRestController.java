package com.achiever.menschenfahren.controller.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.achiever.menschenfahren.base.dto.request.FeedbackCreateDto;
import com.achiever.menschenfahren.base.dto.response.DataResponse;
import com.achiever.menschenfahren.base.dto.response.FeedbackDto;
import com.achiever.menschenfahren.constants.Constants;
import com.achiever.menschenfahren.controller.FeedbackRestControllerInterface;
import com.achiever.menschenfahren.dao.FeedbackDaoInterface;
import com.achiever.menschenfahren.entities.feedback.Feedback;
import com.achiever.menschenfahren.exception.InvalidFeedbackException;
import com.achiever.menschenfahren.mapper.FeedbackMapper;

/**
 * Controller for handling all functionality related to feedback feature.
 * 
 * @author Srijan Bajracharya
 *
 */
@RestController
@RequestMapping(Constants.SERVICE_EVENT_API)
public class FeedbackRestController extends BaseController implements FeedbackRestControllerInterface {

    private final FeedbackDaoInterface feedbackDao;

    private final FeedbackMapper       feedbackMapper;

    @Autowired
    public FeedbackRestController(@Nonnull final FeedbackDaoInterface feedbackDao) {
        this.feedbackDao = feedbackDao;
        this.feedbackMapper = new FeedbackMapper();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<DataResponse<FeedbackDto>> CreateFeedback(@Nonnull final @Valid FeedbackCreateDto request) throws InvalidFeedbackException {

        if (StringUtils.isAnyBlank(request.getUserId(), request.getSubject(), request.getDescription())) {
            throw new InvalidFeedbackException("The input data is invalid");
        }

        Feedback feedback = this.feedbackMapper.map(request, Feedback.class);

        Feedback savedFeedback = this.feedbackDao.save(feedback);

        return buildResponse(this.feedbackMapper.map(savedFeedback, FeedbackDto.class), HttpStatus.CREATED);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<DataResponse<List<FeedbackDto>>> getFeedbacks() {

        List<Feedback> feedbacks = this.feedbackDao.findAll();
        List<FeedbackDto> feedbackDto = new ArrayList<>();

        for (Feedback f : feedbacks) {
            feedbackDto.add(this.feedbackMapper.map(f, FeedbackDto.class));
        }

        if (feedbackDto.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return buildResponse(feedbackDto, HttpStatus.CREATED);
        }
    }

}
