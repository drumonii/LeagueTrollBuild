package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.model.image.ItemImage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = { "from", "into", "lastModifiedDate" })
@ToString
public class Item implements Serializable {

	@Id
	@Column(name = "ID", unique = true, nullable = false)
	@JsonProperty("id")
	@Getter @Setter private int id;

	@Column(name = "NAME")
	@JsonProperty("name")
	@Getter @Setter private String name;

	@Column(name = "ITEM_GROUP")
	@JsonProperty("group")
	@Getter @Setter private String group;

	@Column(name = "CONSUMED")
	@JsonProperty("consumed")
	@Getter @Setter private Boolean consumed;

	@Column(name = "DESCRIPTION")
	@JsonProperty("description")
	@Getter @Setter private String description;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "ITEM_FROM", joinColumns = @JoinColumn(name = "ITEM_ID"))
	@Fetch(FetchMode.SELECT)
	@Column(name = "ITEM_FROM")
	@OrderBy("ITEM_FROM ASC")
	@JsonProperty("from")
	@Getter @Setter private List<String> from;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "ITEM_INTO", joinColumns = @JoinColumn(name = "ITEM_ID"))
	@Column(name = "ITEM_INTO")
	@OrderBy("ITEM_INTO ASC")
	@JsonProperty("into")
	@Getter @Setter private SortedSet<String> into;

	@Column(name = "REQUIRED_CHAMPION")
	@JsonProperty("requiredChampion")
	@Getter @Setter private String requiredChampion;

	@Column(name = "LAST_MODIFIED_DATE", nullable = false)
	@LastModifiedDate
	@JsonIgnore
	@Getter @Setter private Date lastModifiedDate;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "ITEM_MAP", joinColumns = @JoinColumn(name = "ITEM_ID"))
	@MapKeyColumn(name = "MAPS_KEY")
	@Column(name = "MAP")
	@OrderBy("MAPS_KEY ASC")
	@JsonProperty("maps")
	@Getter @Setter private SortedMap<Integer, Boolean> maps;

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
