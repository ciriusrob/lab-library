package io.labforward.api.dtos.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by: Robert Wilson
 * Date:  19/04/2021
 * Project: api
 * Package: io.labforward.api.dtos.request
 * Class: AttributeResponseDto
 */
@Data
public class AttributeResponseDto implements Serializable
{
    private long id;

    private String key;

    private String label;

    private String valueType;

    private Date createdDate;
}
