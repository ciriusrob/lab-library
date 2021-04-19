package io.labforward.api.dtos.response;

import lombok.Data;

/**
 * Created by: Robert Wilson
 * Date:  19/04/2021
 * Project: api
 * Package: io.labforward.api.dtos.response
 * Class: ItemValueResponseDto
 */
@Data
public class ItemValueResponseDto
{
    private long id;

    private long attributeId;

    private String key;

    private String label;

    private String value;
}
