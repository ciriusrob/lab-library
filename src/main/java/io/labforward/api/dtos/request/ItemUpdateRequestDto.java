package io.labforward.api.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by: Robert Wilson
 * Date:  19/04/2021
 * Project: api
 * Package: io.labforward.api.dtos.request
 * Class: ItemUpdateRequestDto
 */
@Data
public class ItemUpdateRequestDto
{
    private String name;

    @NotNull( message = "Item Values Cannot Be Null or Empty")
    private List<ItemValueUpdateRequestDto> itemValues;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemValueUpdateRequestDto
    {
        @Min( value = 1, message = "Id Must Be Greater Than 0")
        private long id;

        @NotEmpty( message = "Value Cannot Be Null or Empty")
        private String value;
    }
}
