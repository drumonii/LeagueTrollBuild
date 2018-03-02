package com.drumonii.loltrollbuild.rest.processor;

import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.util.GameMapUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import java.util.Arrays;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ItemResourceProcessorTest {

	@InjectMocks
	private ItemResourceProcessor resourceProcessor;

	@Mock
	private RepositoryEntityLinks repositoryEntityLinks;

	private Item item = new Item();

	@Before
	public void before() {
		SortedMap<Integer, Boolean> maps = new TreeMap<>();
		maps.put(GameMapUtil.SUMMONERS_RIFT_ID, true);
		maps.put(GameMapUtil.TWISTED_TREELINE_ID, true);
		item.setId(1);
		item.setMaps(maps);

		given(repositoryEntityLinks.linkToSingleResource(eq(Item.class), eq(item.getId())))
				.willReturn(new Link("http://localhost:8080/api/items/" + item.getId()));
		given(repositoryEntityLinks.linkToSingleResource(eq(GameMap.class), eq(GameMapUtil.SUMMONERS_RIFT_ID)))
				.willReturn(new Link("http://localhost:8080/api/maps/" + GameMapUtil.SUMMONERS_RIFT_ID));
		given(repositoryEntityLinks.linkToSingleResource(eq(GameMap.class), eq(GameMapUtil.TWISTED_TREELINE_ID)))
				.willReturn(new Link("http://localhost:8080/api/maps/" + GameMapUtil.TWISTED_TREELINE_ID));
	}

	@Test
	public void processesItemResourceWithNoFromOrInto() {
		Resource<Item> resourceToProcess = new Resource<>(item);

		Resource<Item> processedResource = resourceProcessor.process(resourceToProcess);
		assertThat(processedResource.getLinks()).hasSize(3);
		assertThat(processedResource.getLinks()).extracting(Link::getRel)
				.containsOnly("maps", "maps", "self");
		assertThat(processedResource.getLinks()).extracting(Link::getHref)
				.containsOnly("http://localhost:8080/api/items/" + item.getId(),
						"http://localhost:8080/api/maps/" + GameMapUtil.SUMMONERS_RIFT_ID,
						"http://localhost:8080/api/maps/" + GameMapUtil.TWISTED_TREELINE_ID);
	}

	@Test
	public void processesItemResourceWithFrom() {
		item.setFrom(Arrays.asList(2513, 1026));

		Resource<Item> resourceToProcess = new Resource<>(item);

		given(repositoryEntityLinks.linkToSingleResource(eq(Item.class), eq(2513)))
				.willReturn(new Link("http://localhost:8080/api/items/2513"));
		given(repositoryEntityLinks.linkToSingleResource(eq(Item.class), eq(1026)))
				.willReturn(new Link("http://localhost:8080/api/items/1026"));

		Resource<Item> processedResource = resourceProcessor.process(resourceToProcess);
		assertThat(processedResource.getLinks()).hasSize(5);
		assertThat(processedResource.getLinks()).extracting(Link::getRel)
				.containsOnly("maps", "maps", "from", "from", "self");
		assertThat(processedResource.getLinks()).extracting(Link::getHref)
				.containsOnly("http://localhost:8080/api/items/" + item.getId(),
						"http://localhost:8080/api/maps/" + GameMapUtil.SUMMONERS_RIFT_ID,
						"http://localhost:8080/api/maps/" + GameMapUtil.TWISTED_TREELINE_ID,
						"http://localhost:8080/api/items/2513",
						"http://localhost:8080/api/items/1026");
	}

	@Test
	public void processesItemResourceWithInto() {
		item.setInto(new TreeSet<>(Arrays.asList(4351, 9361)));

		Resource<Item> resourceToProcess = new Resource<>(item);

		given(repositoryEntityLinks.linkToSingleResource(eq(Item.class), eq(4351)))
				.willReturn(new Link("http://localhost:8080/api/items/4351"));
		given(repositoryEntityLinks.linkToSingleResource(eq(Item.class), eq(9361)))
				.willReturn(new Link("http://localhost:8080/api/items/9361"));

		Resource<Item> processedResource = resourceProcessor.process(resourceToProcess);
		assertThat(processedResource.getLinks()).hasSize(5);
		assertThat(processedResource.getLinks()).extracting(Link::getRel)
				.containsOnly("maps", "maps", "into", "into", "self");
		assertThat(processedResource.getLinks()).extracting(Link::getHref)
				.containsOnly("http://localhost:8080/api/items/" + item.getId(),
						"http://localhost:8080/api/maps/" + GameMapUtil.SUMMONERS_RIFT_ID,
						"http://localhost:8080/api/maps/" + GameMapUtil.TWISTED_TREELINE_ID,
						"http://localhost:8080/api/items/4351",
						"http://localhost:8080/api/items/9361");
	}

}