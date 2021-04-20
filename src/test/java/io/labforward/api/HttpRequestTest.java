package io.labforward.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.labforward.api.dtos.request.AttributeRequestDto;
import io.labforward.api.dtos.request.CategoryRequestDto;
import io.labforward.api.dtos.request.ItemRequestDto;
import io.labforward.api.dtos.request.ItemUpdateRequestDto;
import io.labforward.api.dtos.response.AttributeResponseDto;
import io.labforward.api.dtos.response.CategoryResponseDto;
import io.labforward.api.dtos.response.ItemResponseDto;
import io.labforward.api.dtos.response.ItemValueResponseDto;
import io.labforward.api.models.LabHttpResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by: Robert Wilson
 * Date:  20/04/2021
 * Project: api
 * Package: io.labforward.api
 * Class: HttpRequestTest
 */
@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpRequestTest
{
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void createCategoryTest() throws JsonProcessingException
    {
        final String baseUrl = String.format("http://localhost:%d/api/v1", port);

        final ResponseEntity<Object> responseEntity = createAndGetCategory(baseUrl);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void addItemToCategoryTest() throws JsonProcessingException
    {
        final String baseUrl = String.format("http://localhost:%d/api/v1", port);

        final ResponseEntity<Object> categoryResponseEntity = createAndGetCategory(baseUrl);

        final ResponseEntity<Object> responseEntity = createdItemAndGet(baseUrl, categoryResponseEntity);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void getItemsInCategory() throws JsonProcessingException
    {
        final String baseUrl = String.format("http://localhost:%d/api/v1", port);

        final ResponseEntity<Object> categoryResponseEntity = createAndGetCategory(baseUrl);

        createdItemAndGet(baseUrl, categoryResponseEntity);

        final List<ItemResponseDto> items = getItems(baseUrl, categoryResponseEntity);

        assertThat(items.size()).isEqualTo(1);
    }

    @Test
    public void updateItem() throws JsonProcessingException
    {
        String baseUrl = String.format("http://localhost:%d/api/v1", port);

        final ResponseEntity<Object> categoryResponseEntity = createAndGetCategory(baseUrl);

        createdItemAndGet(baseUrl, categoryResponseEntity);

        final List<ItemResponseDto> items = getItems(baseUrl, categoryResponseEntity);

        final ItemResponseDto itemResponseDto = items.get(0);

        final List<ItemValueResponseDto> itemValues = itemResponseDto.getItemValues();

        ItemUpdateRequestDto itemUpdateRequestDto = new ItemUpdateRequestDto();

        itemUpdateRequestDto.setName(itemResponseDto.getName() + " Updated");

        List<ItemUpdateRequestDto.ItemValueUpdateRequestDto> itemValueUpdateRequestDtos = new ArrayList<>();

        itemValues.forEach( itemValueResponseDto -> {

            switch ( itemValueResponseDto.getKey() ) {
                case "colour":
                    itemValueUpdateRequestDtos.add(new ItemUpdateRequestDto.ItemValueUpdateRequestDto(itemValueResponseDto.getId(), "Violet"));
                    break;
                case "quantity":
                    itemValueUpdateRequestDtos.add(new ItemUpdateRequestDto.ItemValueUpdateRequestDto(itemValueResponseDto.getId(), "100"));
                    break;

            }
        } );

        itemUpdateRequestDto.setItemValues(itemValueUpdateRequestDtos);

        final String itemPayload = objectMapper.writeValueAsString(itemUpdateRequestDto);

        final HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<String> requestBody = new HttpEntity<>(itemPayload, headers);

        baseUrl += "/items/" + itemResponseDto.getId();

        final ResponseEntity<Object> responseEntity = restTemplate.exchange(baseUrl, HttpMethod.PUT, requestBody, Object.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private List<ItemResponseDto> getItems( String baseUrl, ResponseEntity<Object> categoryResponseEntity )
    {
        final LabHttpResponse<CategoryResponseDto> apiResponse = objectMapper.convertValue(categoryResponseEntity.getBody(),
            new TypeReference<LabHttpResponse<CategoryResponseDto>>() {
            });

        final CategoryResponseDto categoryResponseDto = apiResponse.getData();

        final long categoryId = categoryResponseDto.getId();

        ResponseEntity<Object> itemsResponse
            = restTemplate.exchange(String.format("%s/categories/%d/items", baseUrl, categoryId), HttpMethod.GET, null, Object.class);

        final LabHttpResponse<List<ItemResponseDto>> apiItemsResponse = objectMapper.convertValue(itemsResponse.getBody(),
            new TypeReference<LabHttpResponse<List<ItemResponseDto>>>() {
            });

        return apiItemsResponse.getData();
    }

    private ResponseEntity<Object> createdItemAndGet( String baseUrl, ResponseEntity<Object> categoryResponseEntity ) throws JsonProcessingException
    {
        final LabHttpResponse<CategoryResponseDto> apiResponse = objectMapper.convertValue(categoryResponseEntity.getBody(),
            new TypeReference<LabHttpResponse<CategoryResponseDto>>() {
            });

        final CategoryResponseDto categoryResponseDto = apiResponse.getData();

        final long categoryId = categoryResponseDto.getId();

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setName("Blood Sample");
        itemRequestDto.setCategoryId(categoryId);

        List<ItemRequestDto.ItemValueRequestDto> itemValueRequestDtos = new ArrayList<>();

        final Set<AttributeResponseDto> attributes = categoryResponseDto.getAttributes();

        attributes.forEach( attribute -> {

            switch ( attribute.getKey() ) {

                case "colour":
                    itemValueRequestDtos.add(new ItemRequestDto.ItemValueRequestDto(attribute.getId(), "Red"));
                    break;
                case "quantity":
                    itemValueRequestDtos.add(new ItemRequestDto.ItemValueRequestDto(attribute.getId(), "5"));
                    break;
            }
        } );

        itemRequestDto.setItemValues(itemValueRequestDtos);

        final String itemPayload = objectMapper.writeValueAsString(itemRequestDto);

        final HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<String> requestBody = new HttpEntity<>(itemPayload, headers);

        return restTemplate.exchange(String.format("%s/items", baseUrl), HttpMethod.POST, requestBody, Object.class);
    }

    private ResponseEntity<Object> createAndGetCategory( String baseUrl ) throws JsonProcessingException
    {
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto();
        categoryRequestDto.setName("SAMPLES");
        categoryRequestDto.setDescription("Samples description");

        Set<AttributeRequestDto> attributeRequestDtos = new HashSet<>();
        attributeRequestDtos.add(new AttributeRequestDto("colour", "Colour", "String"));
        attributeRequestDtos.add(new AttributeRequestDto("quantity", "Quantity", "Integer"));

        categoryRequestDto.setAttributes(attributeRequestDtos);

        final String categoryPayload = objectMapper.writeValueAsString(categoryRequestDto);

        final HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<String> requestBody = new HttpEntity<>(categoryPayload, headers);

        return restTemplate.exchange(baseUrl + "/categories", HttpMethod.POST, requestBody, Object.class);
    }
}
