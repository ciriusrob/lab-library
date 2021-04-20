package io.labforward.api;

import io.labforward.api.controllers.CategoryController;
import static org.assertj.core.api.Assertions.assertThat;

import io.labforward.api.controllers.ItemController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApiTests
{
	@Autowired
	private CategoryController categoryController;

	@Autowired
	private ItemController itemController;

	@Test
	void contextLoads() {

		assertThat(categoryController).isNotNull();

		assertThat(itemController).isNotNull();
	}
}
