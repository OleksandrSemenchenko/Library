package com.nerdysoft.library.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * Mapper interface for the Book entity. The interface definition is used by MapStruct processor to
 * generate BookMapperImpl class under target directory when compiled. Refer https://mapstruct.org
 * for more details.
 *
 * @author Oleksandr Semenchenko
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookMapper {}
