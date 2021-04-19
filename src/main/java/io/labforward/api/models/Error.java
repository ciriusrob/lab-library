package io.labforward.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by: Robert Wilson
 * Date:  19/04/2021
 * Project: api
 * Package: io.labforward.api.models
 * Class: Error
 */
@Data
@AllArgsConstructor
public class Error
{
    private String field;

    private String message;
}