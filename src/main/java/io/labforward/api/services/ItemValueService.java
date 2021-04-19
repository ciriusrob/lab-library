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

    @Autowired
    public ItemValueService( IItemValueRepository repository )
    {
        this.repository = repository;
    }

    @Override
    public Page<ItemValue> all( Pageable pageable )
    {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<ItemValue> single( long id )
    {
        return repository.findById( id );
    }

    @Override
    public ItemValue save( ItemValue entity )
    {
        return repository.save(entity);
    }

    public List<ItemValue> saveAll( Collection<ItemValue> entities )
    {
        return repository.saveAll(entities);
    }

    @Override
    public ItemValue update( ItemValue entity )
    {
        final Optional<ItemValue> existingItemValue = single(entity.getId());

        if ( existingItemValue.isPresent() ) {

            final ItemValue itemValue = existingItemValue.get();

            itemValue.setAttribute( entity.getAttribute() != null ? entity.getAttribute() : itemValue.getAttribute() );

            itemValue.setItem( entity.getItem() != null ? entity.getItem() : itemValue.getItem() );

            itemValue.setValue(StringUtils.hasLength(entity.getValue()) ? entity.getValue() : itemValue.getValue());

            return repository.save(itemValue);
        }

        return null;
    }
}
