package io.labforward.api.controllers;

import io.labforward.api.dtos.request.ItemRequestDto;
import io.labforward.api.dtos.request.ItemUpdateRequestDto;
import io.labforward.api.dtos.response.ItemResponseDto;
import io.labforward.api.dtos.response.ItemValueResponseDto;
import io.labforward.api.entities.Attribute;
import io.labforward.api.entities.Category;
import io.labforward.api.entities.Item;
import io.labforward.api.entities.ItemValue;
import io.labforward.api.models.Error;
import io.labforward.api.services.CategoryService;
import io.labforward.api.services.ItemService;
import io.labforward.api.utils.ApiResponse;
import io.labforward.api.utils.Constant;
import io.labforward.api.utils.Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by: Robert Wilson
 * Date:  19/04/2021
 * Project: api
 * Package: io.labforward.api.controllers
 * Class: ItemController
 */
@RestController
@CrossOrigin( origins = "*" )
@RequestMapping( Constant.BASE_PATH_V1 )
@Api( value = "Item Controller", produces = MediaType.APPLICATION_JSON_VALUE, tags = {"Items"} )
public class ItemController
{
    private ItemService itemService;

    private CategoryService categoryService;


    /**
     * It is a constructor.
     *
     * @param itemService     the item service
     * @param categoryService the category service
     */
    @Autowired
    @Lazy
    public ItemController( ItemService itemService,
                           CategoryService categoryService )
    {

        this.itemService = itemService;
        this.categoryService = categoryService;
    }


    /**
     * Create item
     *
     * @param model                 Request payload (request body) of type ItemRequestDto.
     * @return ResponseEntity<?>    A custom response payload of type ApiResponse
     */
    @ApiOperation( value = "Create New Item", tags = "Items" )
    @PostMapping( value = "/items", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<?> createItem( @Valid @RequestBody ItemRequestDto model )
    {

        final long categoryId = model.getCategoryId();

        final Optional<Category> optionalCategory = categoryService.single(categoryId);

        if ( !optionalCategory.isPresent() ) {
            return new ApiResponse<>(
                HttpStatus.NOT_FOUND,
                Arrays.asList(new Error("categoryId", String.format("Category Not Found By This Id [%d]", categoryId)))
            );
        }

        final Category category = optionalCategory.get();

        final Set<Attribute> categoryAttributes = category.getAttributes();

        final long[] categoryAttributeIds = categoryAttributes.stream().mapToLong(Attribute::getId).toArray();

        final List<ItemValue> modelItemValues = itemService.validateItemValues(categoryAttributeIds, model.getItemValues());

        final ModelMapper mapper = new ModelMapper();

        final Item item = mapper.map(model, Item.class);

        item.setCategory(category);

        item.setItemValues(modelItemValues);

        final Item savedItem = itemService.save(item);

        final PropertyMap<ItemValue, ItemValueResponseDto> itemValuePropertyMap = itemService.getItemValueForItemDtoPropertyMap();

        mapper.addMappings(itemValuePropertyMap);

        Type type = new TypeToken<List<ItemValueResponseDto>>()
        {
        }.getType();

        final List<ItemValue> itemValues = savedItem.getItemValues();

        final List<ItemValueResponseDto> itemValuesResponse = mapper.map(itemValues, type);

        final ItemResponseDto response = mapper.map(savedItem, ItemResponseDto.class);

        response.setItemValues(itemValuesResponse);

        return new ApiResponse<>(
            HttpStatus.CREATED,
            response,
            null
        );
    }

    /**
     *
     * Update item
     *
     * @param  itemId               The ID of the item to update
     * @param  model                The Request payload (request body) of type ItemUpdateRequestDto
     * @return ResponseEntity<?>    A custom response payload of type ApiResponse
     */
    @ApiOperation( value = "Update existing Item", tags = "Items" )
    @PutMapping( value = "/items/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<?> updateItem( @PathVariable( "itemId" ) long itemId, @Valid @RequestBody ItemUpdateRequestDto model )
    {

        final Optional<Item> optionalItem = itemService.single(itemId);

        if ( !optionalItem.isPresent() ) {
            return new ApiResponse<>(
                HttpStatus.NOT_FOUND,
                Arrays.asList(new Error("item", String.format("Item Not Found By This Id [%d]", itemId)))
            );
        }

        final Item item = optionalItem.get();

        item.setName(model.getName());

        final List<ItemUpdateRequestDto.ItemValueUpdateRequestDto> modelItemValues = model.getItemValues();

        if ( modelItemValues != null && modelItemValues.size() > 0 ) {

            modelItemValues.forEach(iv -> {

                final long itemValueId = iv.getId();

                final int index = Util.indexOf(item.getItemValues(), itemValue -> itemValue.getId() == itemValueId);

                if ( index > -1 ) {

                    final ItemValue existingItemValue = item.getItemValues().stream().filter(itemValue -> itemValue.getId() == itemValueId).findFirst().orElse(null);

                    if ( existingItemValue != null ) {

                        existingItemValue.setValue(iv.getValue());

                        item.getItemValues().set(index, existingItemValue);
                    }
                }
            });
        }

        final Item updateItem = itemService.update(item);

        final PropertyMap<ItemValue, ItemValueResponseDto> itemValuePropertyMap = itemService.getItemValueForItemDtoPropertyMap();

        final ModelMapper mapper = new ModelMapper();

        mapper.addMappings(itemValuePropertyMap);

        Type setType = new TypeToken<List<ItemValueResponseDto>>()
        {
        }.getType();

        final List<ItemValue> itemValues = updateItem.getItemValues();

        final List<ItemValueResponseDto> itemValuesResponse = mapper.map(itemValues, setType);

        final ItemResponseDto response = mapper.map(updateItem, ItemResponseDto.class);

        response.setItemValues(itemValuesResponse);

        return new ApiResponse<>(
            HttpStatus.OK,
            response,
            null
        );
    }
}
