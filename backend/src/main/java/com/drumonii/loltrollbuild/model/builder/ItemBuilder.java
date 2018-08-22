package com.drumonii.loltrollbuild.model.builder;

import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.model.ItemGold;
import com.drumonii.loltrollbuild.model.image.ItemImage;

import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Builder for {@link Item}s.
 */
public final class ItemBuilder {

	private int id;
	private String name;
	private String group;
	private Boolean consumed;
	private String description;
	private List<Integer> from = new ArrayList<>();
	private SortedSet<Integer> into = new TreeSet<>();
	private String requiredChampion;
	private String requiredAlly;
	private SortedSet<String> tags = new TreeSet<>();
	private SortedMap<Integer, Boolean> maps = new TreeMap<>();
	private ItemImage image;
	private ItemGold gold;

	public ItemBuilder withId(int id) {
		this.id = id;
		return this;
	}

	public ItemBuilder withName(String name) {
		this.name = name;
		return this;
	}

	public ItemBuilder withGroup(String group) {
		this.group = group;
		return this;
	}

	public ItemBuilder withConsumed(Boolean consumed) {
		this.consumed = consumed;
		return this;
	}

	public ItemBuilder withDescription(String description) {
		this.description = description;
		return this;
	}

	public ItemBuilder withFrom(List<Integer> from) {
		this.from = from;
		return this;
	}

	public ItemBuilder withFrom(Integer... from) {
		if (from != null) {
			withFrom(Arrays.stream(from).collect(Collectors.toCollection(ArrayList::new)));
		}
		return this;
	}

	public ItemBuilder withInto(SortedSet<Integer> into) {
		this.into = into;
		return this;
	}

	public ItemBuilder withInto(Integer... into) {
		if (into != null) {
			withInto(new TreeSet<>(Arrays.asList(into)));
		}
		return this;
	}

	public ItemBuilder withRequiredChampion(String requiredChampion) {
		this.requiredChampion = requiredChampion;
		return this;
	}

	public ItemBuilder withRequiredAlly(String requiredAlly) {
		this.requiredAlly = requiredAlly;
		return this;
	}
	public ItemBuilder withTags(SortedSet<String> tags) {
		this.tags = tags;
		return this;
	}

	public ItemBuilder withTags(String... tags) {
		withTags(new TreeSet<>(Arrays.asList(tags)));
		return this;
	}

	public ItemBuilder withMaps(SortedMap<Integer, Boolean> maps) {
		this.maps = maps;
		return this;
	}

	@SafeVarargs
	public final ItemBuilder withMapEntries(SimpleEntry<Integer, Boolean>... maps) {
		this.maps = Stream.of(maps)
				.collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue, (k, v) -> v, TreeMap::new));
		return this;
	}

	public ItemBuilder withImage(ItemImage image) {
		this.image = image;
		return this;
	}

	public ItemBuilder withGold(ItemGold gold) {
		this.gold = gold;
		return this;
	}

	public Item build() {
		Item item = new Item();
		item.setId(id);
		item.setName(name);
		item.setGroup(group);
		item.setConsumed(consumed);
		item.setDescription(description);
		item.setFrom(from);
		item.setInto(into);
		item.setRequiredChampion(requiredChampion);
		item.setRequiredAlly(requiredAlly);
		item.setTags(tags);
		item.setMaps(maps);
		item.setImage(image);
		if (image != null) {
			image.setId(id);
			image.setItem(item);
		}
		item.setGold(gold);
		if (gold != null) {
			gold.setId(id);
			gold.setItem(item);
		}
		return item;
	}

}
