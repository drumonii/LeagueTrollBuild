package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.model.image.ItemImage;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * League of Legends Item.
 *
 * @see <a href="http://leagueoflegends.wikia.com/wiki/Item">Item Wiki</a>
 */
@Entity
@Table(name = "ITEM")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Item {

	@Id
	@Column(name = "ID", unique = true, nullable = false)
	@JsonProperty("id")
	@Getter @Setter private int id;

	@Column(name = "NAME", nullable = false)
	@JsonProperty("name")
	@Getter @Setter private String name;

	@Column(name = "ITEM_GROUP")
	@JsonProperty("group")
	@Getter @Setter private String group;

	@Column(name = "CONSUMED")
	@JsonProperty("consumed")
	@Getter @Setter private Boolean consumed;

	@Column(name = "DESCRIPTION", nullable = false)
	@JsonProperty("description")
	@Getter @Setter private String description;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "ITEM_FROM",
			joinColumns = @JoinColumn(name = "ITEM_ID"))
	@Column(name = "ITEM_FROM")
	@JsonProperty("from")
	@Getter @Setter private List<String> from;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "ITEM_INTO",
			joinColumns = @JoinColumn(name = "ITEM_ID"))
	@Column(name = "ITEM_INTO")
	@JsonProperty("into")
	@Getter @Setter private Set<String> into;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "ITEM_MAP",
			joinColumns = @JoinColumn(name = "ITEM_ID"))
	@MapKeyColumn(name = "MAPS_KEY")
	@Column(name = "MAP")
	@JsonProperty("maps")
	@Getter @Setter private Map<String, Boolean> maps;

	@OneToOne(cascade = CascadeType.ALL, optional = false)
	@PrimaryKeyJoinColumn
	@JsonManagedReference
	@JsonProperty("image")
	@Getter @Setter private ItemImage image;

	@OneToOne(cascade = CascadeType.ALL, optional = false)
	@PrimaryKeyJoinColumn
	@JsonManagedReference
	@JsonProperty("gold")
	@Getter @Setter private ItemGold gold;

}
