package io.labforward.api.services;

import io.labforward.api.entities.ItemValue;
import io.labforward.api.repositories.IItemValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Created by: Robert Wilson
 * Date:  19/04/2021
 * Project: api
 * Package: io.labforward.api.services
 * Class: ItemValueService
 */
@Service
public class ItemValueService implements IBaseService<ItemValue>
{
    private IItemValueRepository repository;

    /**
     * Constructor
     *
     * @param repository ItemValue repository
     */
    @Autowired
    public ItemValueService( IItemValueRepository repository )
    {
        this.repository = repository;
    }

    /**
     * Fetching a paged collection of ItemValues
     *
     * @param pageable              The pageable object with page, pageSize etc. The repository uses this to make the
     *                              query pageable (limit, offset, orderBy etc)
     * @return Page<ItemValue>      A paged collection of ItemValues
     */
    @Override
    public Page<ItemValue> all( Pageable pageable )
    {
        return repository.findAll(pageable);
    }

    /**
     * Fetching single ItemValue by ID
     *
     * @param id                    The ID of the ItemValue to fetch
     * @return Optional<ItemValue>  An optional ItemValue
     */
    @Override
    public Optional<ItemValue> single( long id )
    {
        return repository.findById(id);
    }


    /**
     * Saving a new ItemValue
     *
     * @param entity        The ItemValue to be saved
     * @return ItemValue    The saved ItemValue with its ID
     */
    @Override
    public ItemValue save( ItemValue entity )
    {
        return repository.save(entity);
    }

    /**
     * Saving a collection of ItemValues at a go
     *
     * @param   entities            A collections of ItemValues to be saved
     * @return List<ItemValue>      A collection of saved ItemValues
     */
    public List<ItemValue> saveAll( Collection<ItemValue> entities )
    {
        return repository.saveAll(entities);
    }

    /**
     * Updating an existing ItemValue
     *
     * @param entity        The ItemValue to be updated
     * @return ItemValue    The updated ItemValue with its ID
     */
    @Override
    public ItemValue update( ItemValue entity )
    {
        final Optional<ItemValue> existingItemValue = single(entity.getId());

        if ( existingItemValue.isPresent() ) {

            final ItemValue itemValue = existingItemValue.get();

            itemValue.setAttribute(entity.getAttribute() != null ? entity.getAttribute() : itemValue.getAttribute());

            itemValue.setItem(entity.getItem() != null ? entity.getItem() : itemValue.getItem());

            itemValue.setValue(StringUtils.hasLength(entity.getValue()) ? entity.getValue() : itemValue.getValue());

            return repository.save(itemValue);
        }

        return null;
    }
}
