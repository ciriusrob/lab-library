package io.labforward.api.services;

import io.labforward.api.entities.Attribute;
import io.labforward.api.repositories.IAttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by: Robert Wilson
 * Date:  19/04/2021
 * Project: api
 * Package: io.labforward.api.services
 * Class: AttributeService
 */
@Service
public class AttributeService implements IBaseService<Attribute>
{
    private IAttributeRepository repository;

    /**
     * Constructor
     *
     * @param repository The Attribute repository
     */
    @Autowired
    public AttributeService( IAttributeRepository repository )
    {
        this.repository = repository;
    }

    /**
     * Fetching a paged collection of Attributes
     *
     * @param pageable          The pageable object with page, pageSize etc. The repository uses this to make the
     *                          query pageable (limit, offset, orderBy etc)
     * @return Page<Attribute>  A paged collection of Attributes
     */
    @Override
    public Page<Attribute> all( Pageable pageable )
    {
        return repository.findAll(pageable);
    }

    /**
     * Getting a single Attribute by ID
     *
     * @param   id                    The ID of the Attribute to fetch
     * @return  Optional<Attribute>   An optional Attribute
     */
    @Override
    public Optional<Attribute> single( long id )
    {
        return repository.findById(id);
    }

    /**
     * Saving a new Attribute
     *
     * @param   entity                The Attribute to be saved
     * @return  Attribute             The saved Attribute with its ID
     */
    @Override
    public Attribute save( Attribute entity )
    {
        return repository.save(entity);
    }

    /**
     * Saves a collection of Attributes at a go
     *
     * @param   entities              The collection of Attributes to be saved
     * @return Set<Attribute>         Returns a set of saved Attributes
     */
    public Set<Attribute> saveAll( Set<Attribute> entities )
    {
        final List<Attribute> saved = repository.saveAll(entities);

        return new HashSet<>(saved);
    }

    /**
     * Updates an Attribute Object
     *
     * @param   entity                The Attribute to be updated
     * @return  Attribute             The updated Attribute
     */
    @Override
    public Attribute update( Attribute entity )
    {

        final Optional<Attribute> existingAttribute = single(entity.getId());

        if ( existingAttribute.isPresent() ) {

            final Attribute attribute = existingAttribute.get();

            attribute.setId(entity.getId());

            attribute.setKey(StringUtils.hasLength(entity.getKey()) ? entity.getKey() : attribute.getKey());

            attribute.setLabel(StringUtils.hasLength(entity.getLabel()) ? entity.getLabel() : attribute.getLabel());

            attribute.setValueType(StringUtils.hasLength(entity.getValueType()) ? entity.getValueType() : attribute.getValueType());

            return repository.save(attribute);
        }

        return null;
    }
}
