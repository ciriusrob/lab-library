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

    @Autowired
    public AttributeService( IAttributeRepository repository )
    {
        this.repository = repository;
    }

    @Override
    public Page<Attribute> all( Pageable pageable )
    {
        return repository.findAll( pageable );
    }

    @Override
    public Optional<Attribute> single( long id )
    {
        return repository.findById( id );
    }

    @Override
    public Attribute save( Attribute entity )
    {
        return repository.save(entity);
    }

    public Set<Attribute> saveAll( Set<Attribute> entities )
    {
        final List<Attribute> saved = repository.saveAll(entities);

        return new HashSet<>(saved);
    }

    @Override
    public Attribute update( Attribute entity )
    {
        final Optional<Attribute> existingAttribute = single(entity.getId());

        if ( existingAttribute.isPresent() ) {

            final Attribute attribute = existingAttribute.get();

            attribute.setId( entity.getId() );

            attribute.setKey(StringUtils.hasLength( entity.getKey() ) ? entity.getKey() : attribute.getKey());

            attribute.setLabel( StringUtils.hasLength(entity.getLabel()) ? entity.getLabel() : attribute.getLabel() );

            attribute.setValueType( StringUtils.hasLength( entity.getValueType() ) ? entity.getValueType() : attribute.getValueType() );

            return repository.save(attribute);
        }

        return null;
    }
}
