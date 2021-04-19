package io.labforward.api.dtos.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by: Robert Wilson
 * Date:  19/04/2021
 * Project: api
 * Package: io.labforward.api.dtos.request
 * Class: CategoryRequestDto
 */
@Data
public class CategoryRequestDto implements Serializable
{
    @NotEmpty( message = "Name Cannot Be Null or Empty")
    private String name;

    private String description;

    @NotNull( message = "Attribute Cannot Be Null or Empty")
    private Set<AttributeRequestDto> attributes;
}
