package io.labforward.api.controllers;

import io.labforward.api.dtos.request.CategoryRequestDto;
import io.labforward.api.dtos.response.CategoryResponseDto;
import io.labforward.api.dtos.response.ItemCollectionResponseDto;
import io.labforward.api.dtos.response.ItemValueResponseDto;
import io.labforward.api.entities.Category;
import io.labforward.api.entities.Item;
import io.labforward.api.entities.ItemValue;
import io.labforward.api.services.CategoryService;
import io.labforward.api.services.ItemService;
import io.labforward.api.utils.ApiResponse;
import io.labforward.api.utils.Constant;
import io.labforward.api.utils.PagedApiResponse;
import io.labforward.api.utils.Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by: Robert Wilson
 * Date:  19/04/2021
 * Project: api
 * Package: io.labforward.api.controllers
 * Class: CategoryController
 */
@RestController
@CrossOrigin( origins = "*" )
@RequestMapping( Constant.BASE_PATH_V1 )
@Api(value = "Category Controller", produces = MediaType.APPLICATION_JSON_VALUE, tags = {"Categories"})
public class CategoryController
{
    private CategoryService categoryService;

    private ItemService itemService;

    /**
     *
     * It is a constructor.
     *
     * @param categoryService   the category service
     * @param itemService       the item service
     */
    @Autowired
    public CategoryController( CategoryService categoryService, ItemService itemService )
    {

        this.categoryService = categoryService;
        this.itemService = itemService;
    }

    /**
     *
     * Create category
     *
     * @param model                 Request payload (request body) of type CategoryRequestDto
     * @return ResponseEntity<?>    A custom response payload of type ApiResponse
     */
    @ApiOperation( value = "Create New Category With Its Attribute Definitions", tags = "Categories" )
    @PostMapping( value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<?> createCategory( @Valid @RequestBody CategoryRequestDto model )
    {
        final ModelMapper mapper = new ModelMapper();

        final Category category = mapper.map(model, Category.class);

        final Category saved = categoryService.save(category);

        final CategoryResponseDto response = mapper.map(saved, CategoryResponseDto.class);

        return new ApiResponse<>(
            HttpStatus.CREATED,
            response,
            null
        );
    }

    /**
     *
     * Gets the category items
     *
     * @param  categoryId           The ID of the category
     * @param  page                 The page number of the pagination step
     * @param  pageSize             The number of items to fetch per page
     * @param  direction            The direction to sort the collection [ASC|DESC]
     * @param  properties           The properties to sort by eg. id,name
     * @return ResponseEntity<?>    A custom response payload of type PagedApiResponse
     */
    @GetMapping( value = "/categories/{categoryId}/items", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<?> getCategoryItems( @PathVariable("categoryId") long categoryId, Integer page, Integer pageSize,
                                               String direction, String... properties )
    {

        properties = Util.toUnderscoredVarArgs(properties, "id");

        Util.PaginationParams paginationParams = new Util.PaginationParams(page, pageSize, direction, properties).invoke();

        PageRequest pageRequest = paginationParams.getPageRequest();

        final Page<Item> itemsByCategoryIdPage = itemService.getAllByCategoryId(categoryId, pageRequest);

        List<ItemCollectionResponseDto> response = new ArrayList<>();

        if ( !itemsByCategoryIdPage.isEmpty() ) {

            final ModelMapper mapper = new ModelMapper();

            final PropertyMap<ItemValue, ItemValueResponseDto> itemValueForItemDtoPropertyMap = itemService.getItemValueForItemDtoPropertyMap();

            mapper.addMappings(itemValueForItemDtoPropertyMap);

            itemsByCategoryIdPage.getContent().forEach(item -> {

                final List<ItemValue> itemValues = item.getItemValues();

                Type type = new TypeToken<List<ItemValueResponseDto>>() {}.getType();

                List<ItemValueResponseDto> mappedItemValues = mapper.map(itemValues, type);

                final ItemCollectionResponseDto mappedItem = mapper.map(item, ItemCollectionResponseDto.class);

                mappedItem.setItemValues(mappedItemValues);

                response.add(mappedItem);
            });
        }

        properties = paginationParams.getProperties();
        page = paginationParams.getPage();
        pageSize = paginationParams.getPageSize();

        return new PagedApiResponse<>(
            ++page,
            pageSize,
            paginationParams.getSortDirection(),
            itemsByCategoryIdPage.getTotalElements(),
            properties,
            HttpStatus.OK,
            response,
            null
        );
    }
}
