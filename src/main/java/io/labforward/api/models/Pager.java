package io.labforward.api.models;

import org.springframework.data.domain.Sort;

/**
 * Created by: Robert Wilson
 * Date:  19/04/2021
 * Project: api
 * Package: io.labforward.api.models
 * Class: Pager
 */
public class Pager
{
    private int page;

    private int pageSize;

    private long totalCount;

    private Sort.Direction direction;

    private String[] sortProperties;

    public Pager( int page, int pageSize, Sort.Direction direction, long totalCount, String ...sortProperties )
    {
        this.page = page;
        this.pageSize = pageSize;
        this.direction = direction;
        this.sortProperties = sortProperties;
        this.totalCount = totalCount;
    }

    public long getTotalCount()
    {
        return totalCount;
    }

    public int getPage()
    {
        return page;
    }

    public int getPageSize()
    {
        return pageSize;
    }

    public Sort.Direction getDirection()
    {
        return direction;
    }

    public String[] getSortProperties()
    {
        return sortProperties;
    }
}

