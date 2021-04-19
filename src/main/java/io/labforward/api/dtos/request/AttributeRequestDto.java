package io.labforward.api.dtos.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * Created by: Robert Wilson
 * Date:  19/04/2021
 * Project: api
 * Package: io.labforward.api.dtos.request
 * Class: AttributeRequestDto
 */
@Data
public class AttributeRequestDto implements Serializable
{
    @NotEmpty( message = "Key Cannot Be Null or Empty")
    private String key;

    private String label;

    @NotEmpty( message = "Value Type Cannot Be Null or Empty")
    private String valueType;
}
