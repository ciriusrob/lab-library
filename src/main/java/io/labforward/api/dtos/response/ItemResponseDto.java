package io.labforward.api.dtos.response;

import lombok.Data;

import java.util.List;

/**
 * Created by: Robert Wilson
 * Date:  19/04/2021
 * Project: api
 * Package: io.labforward.api.dtos.response
 * Class: ItemResponseDto
 */
@Data
public class ItemResponseDto
{
    private long id;

    private String name;

    private CategoryResponseDto category;

    private List<ItemValueResponseDto> itemValues;
}
