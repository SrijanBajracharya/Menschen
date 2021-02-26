package com.achiever.menschenfahren.mapper;

import com.achiever.menschenfahren.base.dto.request.FeedbackCreateDto;
import com.achiever.menschenfahren.base.dto.response.FeedbackDto;
import com.achiever.menschenfahren.entities.feedback.Feedback;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;

public class FeedbackMapper extends ConfigurableMapper {

    @Override
    public void configure(final MapperFactory factory) {

        factory.classMap(Feedback.class, FeedbackDto.class).byDefault().register();
        factory.classMap(Feedback.class, FeedbackCreateDto.class).byDefault().register();
    }

}
