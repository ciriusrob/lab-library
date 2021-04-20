package io.labforward.api.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * Created by: Robert Wilson
 * Date:  19/04/2021
 * Project: api
 * Package: io.labforward.api.dtos.request
 * Class: AttributeRequestDto
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AttributeRequestDto implements Serializable
{
    @NotEmpty( message = "Key Cannot Be Null or Empty")
    private String key;

    private String label;

    @NotEmpty( message = "Value Type Cannot Be Null or Empty")
    private String valueType;
}
