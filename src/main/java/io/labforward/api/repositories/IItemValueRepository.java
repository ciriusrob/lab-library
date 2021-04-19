package io.labforward.api.repositories;

import io.labforward.api.entities.ItemValue;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by: Robert Wilson
 * Date:  19/04/2021
 * Project: api
 * Package: io.labforward.api.repositories
 * Class: IItemValueRepository
 */
public interface IItemValueRepository extends JpaRepository<ItemValue, Long>
{
}
