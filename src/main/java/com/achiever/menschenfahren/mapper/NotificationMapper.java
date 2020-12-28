package com.achiever.menschenfahren.mapper;

import com.achiever.menschenfahren.base.dto.request.NotificationCreateDto;
import com.achiever.menschenfahren.base.dto.request.NotificationEditDto;
import com.achiever.menschenfahren.base.dto.response.EventDto;
import com.achiever.menschenfahren.base.dto.response.NotificationDto;
import com.achiever.menschenfahren.base.dto.response.UserDto;
import com.achiever.menschenfahren.base.model.NotificationStatus;
import com.achiever.menschenfahren.base.model.NotificationType;
import com.achiever.menschenfahren.entities.notification.Notification;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.Mapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.ConfigurableMapper;
import ma.glasnost.orika.metadata.Type;

public class NotificationMapper extends ConfigurableMapper {

    private UserMapper  userMapper  = new UserMapper();

    private EventMapper eventMapper = new EventMapper();

    @Override
    public void configure(final MapperFactory factory) {

        factory.classMap(Notification.class, NotificationDto.class).byDefault().customize(getNotificationMapper()).register();
        factory.classMap(Notification.class, NotificationCreateDto.class).byDefault().exclude("notificationStatus").exclude("notificationType").register();

        factory.classMap(Notification.class, NotificationEditDto.class).byDefault().customize(new CustomMapper<Notification, NotificationEditDto>() {
            @Override
            public void mapAtoB(Notification a, NotificationEditDto b, MappingContext context) {

                b.setNotificationStatus(a.getNotificationStatus().getValue());
            }

            @Override
            public void mapBtoA(NotificationEditDto a, Notification b, MappingContext context) {
                System.err.println(NotificationStatus.getByName(a.getNotificationStatus()));
                b.setNotificationStatus(NotificationStatus.getByName(a.getNotificationStatus()));
            }

        }).register();
    }

    public class CustomEnumConverter extends CustomConverter<String, Enum> {

        @Override
        public Enum convert(String source, Type<? extends Enum> destinationType, MappingContext mappingContext) {
            try {
                return Enum.valueOf(destinationType.getRawType(), source);
            } catch (IllegalArgumentException ignored) {
                return null;
            }
        }
    }

    private final Mapper<Notification, NotificationDto> getNotificationMapper() {
        return new CustomMapper<Notification, NotificationDto>() {
            @Override
            public void mapAtoB(Notification a, NotificationDto b, MappingContext context) {
                b.setNotificationStatus(a.getNotificationStatus().getValue());
                b.setNotificationType(a.getNotificationType().getValue());
            }

            @Override
            public void mapBtoA(NotificationDto a, Notification b, MappingContext context) {
                b.setNotificationStatus(NotificationStatus.getByName(a.getNotificationStatus()));
                b.setNotificationType(NotificationType.fromString(a.getNotificationType()));
            }

        };
    }

    private final Mapper<Notification, NotificationCreateDto> getNotificationCreateMapper() {
        return new CustomMapper<Notification, NotificationCreateDto>() {
            @Override
            public void mapAtoB(Notification a, NotificationCreateDto b, MappingContext context) {
                b.setNotificationStatus(a.getNotificationStatus().getValue());
                b.setNotificationType(a.getNotificationType().getValue());
            }

            @Override
            public void mapBtoA(NotificationCreateDto a, Notification b, MappingContext context) {
                b.setNotificationStatus(NotificationStatus.getByName(a.getNotificationStatus()));
                b.setNotificationType(NotificationType.fromString(a.getNotificationType()));
            }

        };
    }

    public NotificationDto convertNotificationToNotificationDto(final Notification notification) {
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setAlsoVoided(notification.isVoided());
        notificationDto.setEvent(this.eventMapper.map(notification.getEvent(), EventDto.class));

        notificationDto.setReceiverUser(this.userMapper.map(notification.getOriginalReceiver(), UserDto.class));
        notificationDto.setSenderUser(this.userMapper.map(notification.getOriginalSender(), UserDto.class));
        notificationDto.setNotificationStatus(notification.getNotificationStatus().getValue());
        notificationDto.setNotificationType(notification.getNotificationType().getValue());
        notificationDto.setId(notification.getId());
        notificationDto.setModifiedTimestamp(notification.getModifiedTimestamp());
        return notificationDto;
    }

}
