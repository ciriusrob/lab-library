package io.labforward.api.repositories;

import io.labforward.api.entities.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by: Robert Wilson
 * Date:  19/04/2021
 * Project: api
 * Package: io.labforward.api.repositories
 * Class: IAttributeRepository
 */
public interface IAttributeRepository extends JpaRepository<Attribute, Long>
{
}
