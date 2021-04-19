package io.labforward.api.dtos.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Created by: Robert Wilson
 * Date:  19/04/2021
 * Project: api
 * Package: io.labforward.api.dtos.request
 * Class: CategoryResponseDto
 */
@Data
public class CategoryResponseDto implements Serializable
{
    private long id;

    private String name;

    private String description;

    private Set<AttributeResponseDto> attributes;

    private Date createdDate;
}
