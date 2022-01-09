package com.achiever.menschenfahren.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

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
import com.achiever.menschenfahren.base.dto.request.EventTypeCreateDto;
import com.achiever.menschenfahren.controller.impl.EventTypeController;
import com.achiever.menschenfahren.dao.EventTypeDaoInterface;
import com.achiever.menschenfahren.entities.events.EventType;
import com.achiever.menschenfahren.exception.InvalidEventException;
import com.achiever.menschenfahren.exception.InvalidEventTypeException;
import com.achiever.menschenfahren.service.EventTypeService;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class EventTypeRestControllerTest {

    private static PodamFactory   factory;

    private static final String   eventTypeId = "eventTypeId";
    private static final String   voidedId    = "voidedId";
    private static final String   eventId     = "eventId";

    @MockBean
    private EventTypeDaoInterface eventTypeDao;

    @InjectMocks
    private EventTypeController   restController;

    @SpyBean
    private EventTypeService      eventTypeService;

    @BeforeAll
    protected static void initialize() {
        factory = new PodamFactoryImpl();

        factory.getStrategy().addOrReplaceTypeManufacturer(boolean.class, new CustomBooleanStrategy());
        factory.getStrategy().addOrReplaceTypeManufacturer(Boolean.class, new CustomBooleanStrategy());
    }

    @BeforeEach
    void setUp() throws Exception {

        final EventType eventType = buildEventType();
        eventType.setVoided(false);
        eventType.setId(eventTypeId);

        final EventType voidedEventType = buildEventType();
        voidedEventType.setVoided(true);
        voidedEventType.setId(voidedId);

        final List<EventType> allEventTypes = new ArrayList<>();
        allEventTypes.add(eventType);
        allEventTypes.add(voidedEventType);

        final List<EventType> nonVoidedEventType = new ArrayList<>();
        nonVoidedEventType.add(eventType);

        Mockito.doReturn(allEventTypes).when(eventTypeDao).findAll();
        Mockito.doReturn(nonVoidedEventType).when(eventTypeDao).findByVoided(Mockito.eq(false));

        Mockito.doReturn(nonVoidedEventType).when(eventTypeDao).findByVoided(Mockito.eq(false));

        Mockito.doReturn(null).when(eventTypeDao).findByName(Mockito.any(String.class));

        Mockito.doAnswer(invocation -> invocation.getArgument(0, EventType.class)).when(eventTypeDao).save(Mockito.any(EventType.class));
    }

    private EventType buildEventType() {
        return factory.manufacturePojo(EventType.class);
    }

    private EventTypeCreateDto buildEventTypeCreateDto() {
        return factory.manufacturePojo(EventTypeCreateDto.class);
    }

    /**
     * Gets all event types.
     *
     * @throws InvalidEventException
     */
    @Test
    public void testGetEventTypes() throws InvalidEventException {
        final var response = restController.getEventTypes(false);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

    }

    /**
     * create new event type.
     *
     * @throws InvalidEventTypeException
     */
    @Test
    public void testCreateEventType() throws InvalidEventTypeException {
        final EventTypeCreateDto createDto = buildEventTypeCreateDto();

        final var response = restController.createEventType(createDto, false);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

    }

}