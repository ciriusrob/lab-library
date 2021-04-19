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

    /**
     * Constructor
     *
     * @param repository       The repository
     * @param attributeService The attribute service
     */
    @Autowired
    @Lazy
    public CategoryService( ICategoryRepository repository, AttributeService attributeService )
    {
        this.repository = repository;
        this.attributeService = attributeService;
    }

    /**
     * Fetching a paged collection of Categories
     *
     * @param pageable          The pageable object with page, pageSize etc. The repository uses this to make the query pageable (limit, offset, orderBy etc)
     * @return Page<Category>   A paged collection of Categories
     */
    @Override
    public Page<Category> all( Pageable pageable )
    {
        return repository.findAll(pageable);
    }

    /**
     * Fetching single Category by ID
     *
     * @param   id                   The ID of the Category to fetch
     * @return  Optional<Category>   An optional Category
     */
    @Override
    public Optional<Category> single( long id )
    {
        return repository.findById(id);
    }

    /**
     * Saving a new Category
     *
     * @param   entity        The Category to be saved
     * @return  Category      The saved Category with its ID
     */
    @Override
    public Category save( Category entity )
    {
        final Set<Attribute> attributes = entity.getAttributes();

        final Set<Attribute> definitions = attributeService.saveAll(attributes);

        entity.setAttributes(definitions);

        return repository.save(entity);
    }

    /**
     * Updates an existing Category Object (Since there's no Patch, the update takes into account null values from the client
     * before updating so it doesn't override the existing. I patch should do this better)
     *
     * @param   entity      The Category to be updated
     * @return  Category    The updated Category
     */
    @Override
    public Category update( Category entity )
    {
        final Optional<Category> existingCategory = single(entity.getId());

        if ( existingCategory.isPresent() ) {

            final Category category = existingCategory.get();

            category.setId(entity.getId());

            category.setName(StringUtils.hasLength(entity.getName()) ? entity.getName() : category.getName());

            category.setDescription(StringUtils.hasLength(entity.getDescription()) ? entity.getDescription() : category.getDescription());

            return repository.save(category);
        }

        return null;
    }
}
