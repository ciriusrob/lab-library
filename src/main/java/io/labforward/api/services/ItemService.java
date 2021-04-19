package io.labforward.api.services;

import io.labforward.api.dtos.request.ItemRequestDto;
import io.labforward.api.dtos.response.ItemValueResponseDto;
import io.labforward.api.entities.Attribute;
import io.labforward.api.entities.Item;
import io.labforward.api.entities.ItemValue;
import io.labforward.api.repositories.IItemRepository;
import io.labforward.api.utils.Util;
import org.modelmapper.PropertyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by: Robert Wilson
 * Date:  19/04/2021
 * Project: api
 * Package: io.labforward.api.services
 * Class: ItemService
 */
@Service
public class ItemService implements IBaseService<Item>
{
    private IItemRepository repository;

    private ItemValueService itemValueService;

    private AttributeService attributeService;

    /**
     * Constructor
     *
     * @param repository       The repository
     * @param itemValueService The ItemValue service
     * @param attributeService The Attribute service
     */
    @Autowired
    @Lazy
    public ItemService( IItemRepository repository,
                        ItemValueService itemValueService,
                        AttributeService attributeService )
    {
        this.repository = repository;
        this.itemValueService = itemValueService;
        this.attributeService = attributeService;
    }

    /**
     * Fetching a paged collection of Attributes
     *
     * @param pageable          The pageable object with page, pageSize etc. The repository uses this to make the query pageable (limit, offset, orderBy etc)
     * @return Page<Item>
     */
    @Override
    public Page<Item> all( Pageable pageable )
    {
        return repository.findAll(pageable);
    }

    /**
     * Fetch a single Item by ID
     *
     * @param id                The ID of the Item to fetch
     * @return Optional<Item>   An optional Item
     */
    @Override
    public Optional<Item> single( long id )
    {
        return repository.findById(id);
    }

    /**
     * Saves a new Item
     *
     * @param entity    The Item to be saved
     * @return Item     The saved Item with its ID
     */
    @Override
    public Item save( Item entity )
    {
        final List<ItemValue> itemValues = entity.getItemValues();

        entity.setItemValues(null);

        final Item savedItem = repository.save(entity);

        final List<ItemValue> mappedItemValues = itemValues.stream().map(itemValue -> {
            itemValue.setItem(savedItem);
            return itemValue;
        }).collect(Collectors.toList());

        final List<ItemValue> savedItemValues = itemValueService.saveAll(mappedItemValues);

        savedItem.setItemValues(savedItemValues);

        return savedItem;
    }

    /**
     * Updates an existing Item Object (Since there's no Patch, the update takes into account null values from the client
     * before updating so it doesn't override the existing. I Patch should do this better)
     *
     * @param   entity          The Item to be updated
     * @return  Item            The updated Item
     */
    @Override
    public Item update( Item entity )
    {
        final Optional<Item> existingItem = single(entity.getId());

        if ( existingItem.isPresent() ) {

            final Item item = existingItem.get();

            item.setId(entity.getId());

            item.setCategory(entity.getCategory() != null ? entity.getCategory() : item.getCategory());

            item.setName(StringUtils.hasLength(entity.getName()) ? entity.getName() : item.getName());

            return repository.save(item);
        }

        return null;
    }

    /**
     * Gets the all Items by category ID
     *
     * @param   categoryId    The category ID
     * @param   pageable      The pageable object with page, pageSize etc. The repository uses this to make the query
     *                        pageable (limit, offset, orderBy etc)
     * @return  Page<Item>    A paged collection of Items
     */
    public Page<Item> getAllByCategoryId( long categoryId, Pageable pageable )
    {
        return repository.findAllByCategoryId(categoryId, pageable);
    }

    /**
     * Validate ItemValues (To save an Item, the values of the Item should correspond to the defined attributes of the category )
     *
     * @param categoryAttributeIds An array of category attribute ids
     * @param modelItemValues      A collection of ItemValues from the client
     * @return List<ItemValue>     A collection of validated ItemValue entities
     */
    public List<ItemValue> validateItemValues( long[] categoryAttributeIds, List<ItemRequestDto.ItemValueRequestDto> modelItemValues )
    {
        return modelItemValues.stream().map(iv -> {

            final long attributeId = iv.getAttributeId();

            if ( !Util.contains(categoryAttributeIds, attributeId) ) {
                throw new RuntimeException(String.format("Selected Category Does Not Have This Attribute ID %d", attributeId));
            }

            final Optional<Attribute> attribute = attributeService.single(attributeId);

            if ( !attribute.isPresent() ) {
                throw new RuntimeException(String.format("Could Not Find Attribute ID With %d", attributeId));
            }

            final ItemValue itemValue = new ItemValue();
            itemValue.setValue(iv.getValue());
            itemValue.setAttribute(attribute.get());

            return itemValue;
        }).collect(Collectors.toList());
    }

    /**
     * Flattens an ItemValue into  a DTO (It's a combination of some Category attributes with the actual Item value)
     *
     * @return the item value for item dto property map
     */
    public PropertyMap<ItemValue, ItemValueResponseDto> getItemValueForItemDtoPropertyMap()
    {
        return new PropertyMap<ItemValue, ItemValueResponseDto>()
        {
            @Override
            protected void configure()
            {
                map(source.getId(), destination.getId());
                map(source.getAttribute().getId(), destination.getAttributeId());
                map(source.getAttribute().getKey(), destination.getKey());
                map(source.getAttribute().getLabel(), destination.getLabel());
                map(source.getValue(), destination.getValue());
            }
        };
    }
}
