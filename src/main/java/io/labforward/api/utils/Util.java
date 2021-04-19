package io.labforward.api.utils;

import com.google.common.base.CaseFormat;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by: Robert Wilson
 * Date:  19/04/2021
 * Project: api
 * Package: io.labforward.api.utils
 * Class: Util
 */
public class Util
{

    public static boolean contains( long[] haystack, long needle )
    {
        return Arrays.stream(haystack).anyMatch(l -> l == needle);
    }

    public static boolean contains( String[] properties, String element )
    {
        if ( properties == null || properties.length == 0 ) return false;

        if ( !StringUtils.hasLength(element) ) return false;

        boolean status = false;

        for ( int i = 0; i < properties.length; i++ ) {

            String value = properties[i];

            if ( element.equals(value) ) {
                status = true;
                break;
            }
        }

        return status;
    }

    public static <T> int indexOf( List<T> list, Predicate<? super T> predicate )
    {
        return IntStream.range(0, list.size())
            .filter(ix -> predicate.test(list.get(ix)))
            .findFirst().orElse(-1);
    }

    public static String[] toUnderscoredVarArgs( String[] properties, String defaultProperty )
    {
        return toUnderscoredVarArgsNative(properties, defaultProperty);
    }

    public static String[] toUnderscoredVarArgs( String[] properties, String... defaultProperties )
    {
        return toUnderscoredVarArgsNative(properties, defaultProperties);
    }

    public static String[] toUnderscoredVarArgsNative( String[] properties, String... defaultProperties )
    {
        properties = properties == null ? Arrays.stream(defaultProperties).map(s -> CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, s))
            .toArray(String[]::new) :
            Arrays.stream(properties).map(s -> CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, s))
                .toArray(String[]::new);
        return properties;
    }

    public static class PaginationParams
    {
        private Integer page;
        private Integer pageSize;
        private Sort.Direction sortDirection;
        private String[] properties;
        private PageRequest pageRequest;

        public PaginationParams( Integer page, Integer pageSize, String direction, String... properties )
        {
            this.page = page != null && page > 0 ? --page : 0;
            this.pageSize = pageSize == null ? 10 : pageSize;
            this.sortDirection = Sort.Direction.DESC;

            if ( StringUtils.hasLength(direction) ) {
                if ( direction.toUpperCase().equals("ASC") ) {
                    this.sortDirection = Sort.Direction.ASC;
                }
            }

            this.properties = properties == null ? new String[]{"id"} : properties;
        }

        public Integer getPage()
        {
            return page;
        }

        public Integer getPageSize()
        {
            return pageSize;
        }

        public String[] getProperties()
        {
            return properties;
        }

        public PageRequest getPageRequest()
        {
            return pageRequest;
        }

        public List<Sort.Order> getSortOrders()
        {
            return Arrays.stream(properties).map(p -> {
                if ( p.contains("date") ) {
                    return new Sort.Order(sortDirection, p);
                }
                else {
                    return new Sort.Order(sortDirection, p).ignoreCase();
                }
            }).collect(Collectors.toList());
        }

        public List<Sort.Order> getSortOrders( final Sort.Direction sortDirection, final String... properties)
        {
            return Arrays.stream(properties).map(p -> {
                if ( p.contains("date") ) {
                    return new Sort.Order(sortDirection, p);
                }
                else {
                    return new Sort.Order(sortDirection, p).ignoreCase();
                }
            }).collect(Collectors.toList());
        }

        public Sort.Direction getSortDirection()
        {
            return sortDirection;
        }

        public PaginationParams invoke()
        {
            if (
                Util.contains(properties, "createdDate")  ||
                Util.contains(properties, "created_date") ||
                Util.contains(properties, "updatedDate")  ||
                Util.contains(properties, "updated_date")
            ) {
                pageRequest = PageRequest.of(page, pageSize, sortDirection, properties);
            }
            else {
                pageRequest = PageRequest.of(page, pageSize, Sort.by(getSortOrders()));
            }
            return this;
        }
    }
}
