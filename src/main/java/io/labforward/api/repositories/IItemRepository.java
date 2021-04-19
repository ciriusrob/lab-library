package io.labforward.api.repositories;

import io.labforward.api.entities.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by: Robert Wilson
 * Date:  19/04/2021
 * Project: api
 * Package: io.labforward.api.repositories
 * Class: IItemRepository
 */
public interface IItemRepository extends JpaRepository<Item, Long>
{
    @Query(
        value = "SELECT i.* FROM items i WHERE i.category_id = ?1",
        nativeQuery = true
    )
    Page<Item> findAllByCategoryId( long categoryId, Pageable pageable );
}
