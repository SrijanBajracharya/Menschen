package com.achiever.menschenfahren.controller.impl;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.achiever.menschenfahren.base.controller.SearchConfigurationRestControllerInterface;
import com.achiever.menschenfahren.base.dto.request.SearchConfigCreateDto;
import com.achiever.menschenfahren.base.dto.response.DataResponse;
import com.achiever.menschenfahren.base.dto.response.SearchConfigResponseDto;
import com.achiever.menschenfahren.base.exception.InvalidSearchConfigException;
import com.achiever.menschenfahren.base.exception.ResourceNotFoundException;
import com.achiever.menschenfahren.constants.Constants;
import com.achiever.menschenfahren.dao.SearchConfigDaoInterface;
import com.achiever.menschenfahren.dao.UserDaoInterface;
import com.achiever.menschenfahren.entities.configuration.SearchConfiguration;
import com.achiever.menschenfahren.entities.users.User;
import com.achiever.menschenfahren.mapper.SearchConfigMapper;
import com.achiever.menschenfahren.service.impl.AuthenticationService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(Constants.SERVICE_EVENT_API)
@Slf4j
public class SearchConfigurationRestController extends BaseController implements SearchConfigurationRestControllerInterface {

    private final SearchConfigMapper       searchConfigMapper;

    private final UserDaoInterface         userDao;

    private final SearchConfigDaoInterface searchConfigDao;

    @Autowired
    private AuthenticationService          authenticationService;

    @Autowired
    public SearchConfigurationRestController(@Nonnull final UserDaoInterface userDao, @Nonnull final SearchConfigDaoInterface searchConfigDao) {
        this.searchConfigMapper = new SearchConfigMapper();
        this.userDao = userDao;
        this.searchConfigDao = searchConfigDao;
    }

    @Override
    public ResponseEntity<DataResponse<SearchConfigResponseDto>> createSearchConfig(@Nonnull @Valid final SearchConfigCreateDto request)
            throws InvalidSearchConfigException, ResourceNotFoundException {
        String userId = authenticationService.getId();

        if (StringUtils.isBlank(userId)) {
            log.error("The id of user should not be empty.");
            throw new InvalidSearchConfigException("The user id should not be empty.");
        }

        Optional<User> userOptional = this.userDao.findById(userId);

        if (userOptional.get() == null) {
            log.error("The user not found with id :  {}", request.getUserId());
            throw new ResourceNotFoundException("The user not found with id:" + request.getUserId());
        }

        // set from token
        request.setUserId(userId);

        SearchConfiguration searchConfiguration = this.searchConfigMapper.map(request, SearchConfiguration.class);

        SearchConfiguration savedSearchConfig = this.searchConfigDao.save(searchConfiguration);

        if (savedSearchConfig != null) {
            SearchConfigResponseDto savedDto = this.searchConfigMapper.map(savedSearchConfig, SearchConfigResponseDto.class);
            return buildResponse(savedDto, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

}
