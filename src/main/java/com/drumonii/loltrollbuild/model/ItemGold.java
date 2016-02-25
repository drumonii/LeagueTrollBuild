package com.drumonii.loltrollbuild.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Gold information of an {@link Item}.
 */
@Entity
@Table(name = "ITEM_GOLD")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = { "id", "item" })
@ToString(exclude = { "id", "item" })
public class ItemGold implements Serializable {

	@Id
	@Column(name = "ITEM_ID", unique = true, nullable = false)
	@JsonIgnore
	@Getter @Setter private int id;

	@Column(name = "BASE", nullable = false)
	@JsonProperty("base")
	@Getter @Setter private int base;

	@Column(name = "TOTAL", nullable = false)
	@JsonProperty("total")
	@Getter @Setter private int total;

	@Column(name = "SELL", nullable = false)
	@JsonProperty("sell")
	@Getter @Setter private int sell;

	@Column(name = "PURCHASABLE", nullable = false)
	@JsonProperty("purchasable")
	@Getter @Setter private boolean purchasable;

	@MapsId
	@OneToOne(optional = false, mappedBy = "image")
	@JsonBackReference
	@Getter @Setter private Item item;

}
