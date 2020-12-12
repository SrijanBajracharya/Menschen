package com.achiever.menschenfahren.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;

import com.achiever.menschenfahren.CustomBooleanStrategy;
import com.achiever.menschenfahren.base.dto.EventCreateDto;
import com.achiever.menschenfahren.base.dto.EventDto;
import com.achiever.menschenfahren.base.dto.EventEditDto;
import com.achiever.menschenfahren.controller.impl.EventRestController;
import com.achiever.menschenfahren.dao.EventDaoInterface;
import com.achiever.menschenfahren.dao.UserDaoInterface;
import com.achiever.menschenfahren.entities.events.Event;
import com.achiever.menschenfahren.entities.users.User;
import com.achiever.menschenfahren.exception.InvalidEventException;
import com.achiever.menschenfahren.service.EventService;
import com.achiever.menschenfahren.service.UserService;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class EventRestControllerTest {

    private static PodamFactory factory;

    private static final String userId   = "userId";
    private static final String voidedId = "voidedId";
    private static final String eventId  = "eventId";

    @MockBean
    private EventDaoInterface   eventDao;

    @MockBean
    private UserDaoInterface    userDao;

    @InjectMocks
    private EventRestController restController;

    @SpyBean
    private UserService         userService;

    @SpyBean
    private EventService        eventService;

    @BeforeAll
    protected static void initialize() {
        factory = new PodamFactoryImpl();

        factory.getStrategy().addOrReplaceTypeManufacturer(boolean.class, new CustomBooleanStrategy());
        factory.getStrategy().addOrReplaceTypeManufacturer(Boolean.class, new CustomBooleanStrategy());
    }

    @BeforeEach
    void setUp() throws Exception {

        final User user = buildUser();
        user.setVoided(false);
        user.setId(userId);

        final User voidedUser = buildUser();
        voidedUser.setVoided(true);
        voidedUser.setId(voidedId);

        final Event event = buildEvent();
        event.setId(eventId);
        event.setVoided(false);
        event.setPrivate(false);
        event.setUser(user);

        final Event voidedEvent = buildEvent();
        voidedEvent.setId(eventId);
        voidedEvent.setVoided(true);
        voidedEvent.setPrivate(false);

        final List<Event> allEvents = new ArrayList<>();
        allEvents.add(event);
        allEvents.add(voidedEvent);

        final List<Event> nonVoidedEvent = new ArrayList<>();
        nonVoidedEvent.add(event);

        Mockito.doReturn(allEvents).when(eventDao).findAll();
        Mockito.doReturn(nonVoidedEvent).when(eventDao).findByVoided(Mockito.eq(false));

        Mockito.doAnswer(invocation -> invocation.getArgument(0, Event.class)).when(eventDao).save(Mockito.any(Event.class));
        Mockito.doReturn(true).when(eventDao).existsById(Mockito.eq(eventId));

        Mockito.doReturn(allEvents).when(eventDao).findByVoidedAndIsPrivate(Mockito.eq(false), Mockito.eq(false));

        Mockito.doReturn(Optional.of(event)).when(eventDao).findByIdAndVoidedAndIsPrivate(Mockito.eq(eventId), Mockito.eq(false), Mockito.eq(false));

        Mockito.doReturn(Optional.of(event)).when(eventDao).findById(Mockito.eq(eventId));

        Mockito.doReturn(Optional.of(user)).when(userDao).findById(Mockito.eq(userId));
    }

    private User buildUser() {
        return factory.manufacturePojo(User.class);
    }

    private Event buildEvent() {
        return factory.manufacturePojo(Event.class);
    }

    private EventCreateDto buildEventCreateDto() {
        return factory.manufacturePojo(EventCreateDto.class);
    }

    private EventEditDto buildEventEditDto() {
        return factory.manufacturePojo(EventEditDto.class);
    }

    private EventDto buildEventDto() {
        return factory.manufacturePojo(EventDto.class);
    }

    @Test
    public void testGetEvents() {
        final var response = restController.getEvents(false, false);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

    }

    @Test
    public void testGetEvent() {
        var response = restController.getEvent(eventId, false, false);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        response = restController.getEvent("non-existing", false, false);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testCreateEvent() throws InvalidEventException {
        final EventCreateDto createDto = buildEventCreateDto();
        createDto.setUserId(userId);

        final var response = restController.createEvent(createDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

    }

}
