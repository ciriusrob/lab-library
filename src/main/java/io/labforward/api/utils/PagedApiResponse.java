package io.labforward.api.utils;

import io.labforward.api.models.Error;
import io.labforward.api.models.Pager;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Created by: Robert Wilson
 * Date:  19/04/2021
 * Project: api
 * Package: io.labforward.api.utils
 * Class: PagedApiResponse
 */
public class PagedApiResponse<T> extends ApiResponse<T>
{
    public PagedApiResponse(
        int page,
        int pageSize,
        Sort.Direction direction,
        long totalCount,
        String[] sortProperties,
        HttpStatus code,
        T data,
        List<Error> errors )
    {
        super(code, new Pager(page, pageSize,  direction, totalCount, sortProperties), data,  errors);

    }
}
