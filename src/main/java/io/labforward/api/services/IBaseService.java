package io.labforward.api.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Created by: Robert Wilson
 * Date:  19/04/2021
 * Project: api
 * Package: io.labforward.api.services
 * Class: IBaseService
 */
public interface IBaseService<T>
{
    Page<T> all( Pageable pageable );

    Optional<T> single( long id );

    T save( T entity );

    T update( T entity );
}
