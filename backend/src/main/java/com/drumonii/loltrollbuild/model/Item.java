package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.model.image.ItemImage;
import com.drumonii.loltrollbuild.rest.view.ApiViews;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.persistence.Version;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.SortedMap;
import java.util.SortedSet;

/**
 * League of Legends Item.
 *
 * @see <a href="http://leagueoflegends.wikia.com/wiki/Item">Item Wiki</a>
 */
@Entity
@Table(name = "ITEM")
@EntityListeners(AuditingEntityListener.class)
public class Item implements Serializable {

	@Id
	@Column(name = "ID", unique = true, nullable = false)
	@JsonView({ ApiViews.AllApis.class })
	private int id;

	@Version
	@Column(name = "VERSION", nullable = false)
	@JsonIgnore
	private Long version;

	@Column(name = "NAME")
	@JsonProperty("name")
	@JsonView({ ApiViews.AllApis.class })
	private String name;

	@Column(name = "ITEM_GROUP")
	@JsonProperty("group")
	@JsonView({ ApiViews.AllApis.class })
	private String group;

	@Column(name = "CONSUMED")
	@JsonProperty("consumed")
	@JsonView({ ApiViews.AllApis.class })
	private Boolean consumed;

	@Column(name = "DESCRIPTION")
	@JsonProperty("description")
	@JsonView({ ApiViews.AllApis.class })
	private String description;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "ITEM_FROM", joinColumns = @JoinColumn(name = "ITEM_ID"))
	@Fetch(FetchMode.SELECT)
	@Column(name = "ITEM_FROM")
	@OrderBy("ITEM_FROM ASC")
	@JsonProperty("from")
	@JsonView({ ApiViews.AllApis.class })
	private List<Integer> from;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "ITEM_INTO", joinColumns = @JoinColumn(name = "ITEM_ID"))
	@Column(name = "ITEM_INTO")
	@OrderBy("ITEM_INTO ASC")
	@JsonProperty("into")
	@JsonView({ ApiViews.AllApis.class })
	private SortedSet<Integer> into;

	@Column(name = "REQUIRED_CHAMPION")
	@JsonProperty("requiredChampion")
	@JsonView({ ApiViews.AllApis.class })
	private String requiredChampion;

	@Column(name = "REQUIRED_ALLY")
	@JsonProperty("requiredAlly")
	@JsonView({ ApiViews.AllApis.class })
	private String requiredAlly;

	@Column(name = "LAST_MODIFIED_DATE", nullable = false)
	@LastModifiedDate
	@JsonIgnore
	private LocalDateTime lastModifiedDate;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "ITEM_MAP", joinColumns = @JoinColumn(name = "ITEM_ID"))
	@MapKeyColumn(name = "MAPS_KEY")
	@Column(name = "MAP")
	@OrderBy("MAPS_KEY ASC")
	@JsonProperty("maps")
	@JsonView({ ApiViews.AllApis.class })
	private SortedMap<Integer, Boolean> maps;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "item")
	@JsonManagedReference
	@JsonProperty("image")
	@JsonView({ ApiViews.RiotApi.class })
	private ItemImage image;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "item")
	@JsonManagedReference
	@JsonProperty("gold")
	@JsonView({ ApiViews.AllApis.class })
	private ItemGold gold;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public Boolean getConsumed() {
		return consumed;
	}

	public void setConsumed(Boolean consumed) {
		this.consumed = consumed;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Integer> getFrom() {
		return from;
	}

	public void setFrom(List<Integer> from) {
		this.from = from;
	}

	public SortedSet<Integer> getInto() {
		return into;
	}

	public void setInto(SortedSet<Integer> into) {
		this.into = into;
	}

	public String getRequiredChampion() {
		return requiredChampion;
	}

	public void setRequiredChampion(String requiredChampion) {
		this.requiredChampion = requiredChampion;
	}

	public String getRequiredAlly() {
		return requiredAlly;
	}

	public void setRequiredAlly(String requiredAlly) {
		this.requiredAlly = requiredAlly;
	}

	public LocalDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public SortedMap<Integer, Boolean> getMaps() {
		return maps;
	}

	public void setMaps(SortedMap<Integer, Boolean> maps) {
		this.maps = maps;
	}

	public ItemImage getImage() {
		return image;
	}

	public void setImage(ItemImage image) {
		this.image = image;
	}

	public ItemGold getGold() {
		return gold;
	}

	public void setGold(ItemGold gold) {
		this.gold = gold;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Item item = (Item) o;
		return id == item.id &&
				Objects.equals(name, item.name) &&
				Objects.equals(group, item.group) &&
				Objects.equals(consumed, item.consumed) &&
				Objects.equals(description, item.description) &&
				Objects.equals(requiredChampion, item.requiredChampion) &&
				Objects.equals(requiredAlly, item.requiredAlly) &&
				Objects.equals(maps, item.maps) &&
				Objects.equals(image, item.image) &&
				Objects.equals(gold, item.gold);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, group, consumed, description, requiredChampion, requiredAlly, maps, image, gold);
	}

	@Override
	public String toString() {
		return "Item{" +
				"id=" + id +
				", version=" + version +
				", name='" + name + '\'' +
				", group='" + group + '\'' +
				", consumed=" + consumed +
				", description='" + description + '\'' +
				", from=" + from +
				", into=" + into +
				", requiredChampion='" + requiredChampion + '\'' +
				", requiredAlly='" + requiredAlly + '\'' +
				", lastModifiedDate=" + lastModifiedDate +
				", maps=" + maps +
				", image=" + image +
				", gold=" + gold +
				'}';
	}

}
