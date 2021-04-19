package io.labforward.api.services;

import io.labforward.api.entities.Attribute;
import io.labforward.api.entities.Category;
import io.labforward.api.repositories.ICategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.Set;

/**
 * Created by: Robert Wilson
 * Date:  19/04/2021
 * Project: api
 * Package: io.labforward.api.services
 * Class: CategoryService
 */
@Service
public class CategoryService implements IBaseService<Category>
{
    private ICategoryRepository repository;

    private AttributeService attributeService;

    @Autowired
    @Lazy
    public CategoryService( ICategoryRepository repository, AttributeService attributeService )
    {
        this.repository = repository;
        this.attributeService = attributeService;
    }

    @Override
    public Page<Category> all( Pageable pageable )
    {
        return repository.findAll( pageable );
    }

    @Override
    public Optional<Category> single( long id )
    {
        return repository.findById( id );
    }

    @Override
    public Category save( Category entity )
    {
        final Set<Attribute> attributes = entity.getAttributes();

        final Set<Attribute> definitions = attributeService.saveAll(attributes);

        entity.setAttributes(definitions);

        return repository.save(entity);
    }

    @Override
    public Category update( Category entity )
    {
        final Optional<Category> existingCategory = single(entity.getId());

        if ( existingCategory.isPresent() ) {

            final Category category = existingCategory.get();

            category.setId( entity.getId() );

            category.setName(StringUtils.hasLength( entity.getName()) ? entity.getName() : category.getName() );

            category.setDescription( StringUtils.hasLength(entity.getDescription()) ? entity.getDescription() : category.getDescription() );

            return repository.save(category);
        }

        return null;
    }
}
