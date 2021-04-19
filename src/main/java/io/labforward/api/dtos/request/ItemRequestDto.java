package io.labforward.api.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * Created by: Robert Wilson
 * Date:  19/04/2021
 * Project: api
 * Package: io.labforward.api.dtos.request
 * Class: ItemRequestDto
 */
@Data
public class ItemRequestDto implements Serializable
{
    @NotEmpty( message = "Name Cannot Be Null or Empty")
    private String name;

    @Min( value = 1, message = "Category Id Must Be Greater Than 0")
    private long categoryId;

    @NotNull( message = "Item Values Cannot Be Null or Empty")
    private List<ItemValueRequestDto> itemValues;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemValueRequestDto
    {
        @Min( value = 1, message = "Category Id Must Be Greater Than 0")
        private long attributeId;

        @NotEmpty( message = "Value Cannot Be Null or Empty")
        private String value;
    }
}
