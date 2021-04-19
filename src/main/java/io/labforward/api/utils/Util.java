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
    /**
     *
     * The contains method is used to check whether a long is in an array of longs
     *
     * @param haystack      the haystack (An array of longs to check in)
     * @param needle        the needle (The long to look for)
     * @return boolean
     */
    public static boolean contains( long[] haystack, long needle )
    {
        return Arrays.stream(haystack).anyMatch(l -> l == needle);
    }

    /**
     *
     * The contains method is used to check whether a String is in an array of Strings (It's basically an overloaded
     * version of the contains method above, just that it accepts a different data type)
     *
     * @param haystack      the haystack (An array of Strings to check in)
     * @param needle        the needle (The String to look for)
     * @return boolean
     */
    public static boolean contains( String[] haystack, String needle )
    {
        if ( haystack == null || haystack.length == 0 ) return false;

        if ( !StringUtils.hasLength(needle) ) return false;

        boolean status = false;

        for ( int i = 0; i < haystack.length; i++ ) {

            String value = haystack[i];

            if ( needle.equals(value) ) {
                status = true;
                break;
            }
        }

        return status;
    }

    /**
     *
     * A custom indexOf method that accepts a Predicate
     *
     * @param list          The collection of type T to use
     * @param predicate     The predicate to test a condition
     * @return int          The index of the matched element else -1
     */
    public static <T> int indexOf( List<T> list, Predicate<? super T> predicate )
    {
        return IntStream.range(0, list.size())
            .filter(ix -> predicate.test(list.get(ix)))
            .findFirst().orElse(-1);
    }

    /**
     *
     * This method transforms a sort property into a database column
     *
     * @param properties            The properties on the entity to sort by (eg. id,name,createdDate)
     * @param defaultProperties     the default sort property
     * @return String[]             A String array of transformed properties
     */
    public static String[] toUnderscoredVarArgs( String[] properties, String... defaultProperties )
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

        /**
         *
         * Constructor for the Pagination helper class (It sets default values for pagination params a collection endpoints)
         *
         * @param page              the page value of the pagination param (Default 0)
         * @param pageSize          the page size value pagination param (Default 10)
         * @param direction         the direction value pagination param (Default DESC)
         * @param properties        the properties value to sort by (Default id)
         */
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

        /**
         *
         * Gets the page
         *
         * @return the page value
         */
        public Integer getPage()
        {
            return page;
        }

        /**
         *
         * Gets the page size
         *
         * @return the page size value
         */
        public Integer getPageSize()
        {
            return pageSize;
        }

        /**
         *
         * Gets the properties
         *
         * @return the properties
         */
        public String[] getProperties()
        {
            return properties;
        }

        /**
         *
         * Gets the page request
         *
         * @return the page request Object
         */
        public PageRequest getPageRequest()
        {
            return pageRequest;
        }

        /**
         *
         * Gets the pagination sort orders
         *
         * @return the sort orders
         */
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

        /**
         *
         * Gets the pagination sort direction
         *
         * @return the sort direction
         */
        public Sort.Direction getSortDirection()
        {
            return sortDirection;
        }

        /**
         *
         * Invoke
         *
         * @return PaginationParams
         */
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
