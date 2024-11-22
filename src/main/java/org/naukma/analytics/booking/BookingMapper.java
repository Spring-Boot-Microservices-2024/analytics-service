package org.naukma.analytics.booking;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    BookingDto entityToDto(BookingEntity bookingEntity);

    BookingEntity dtoToEntity(BookingDto bookingDto);
}
