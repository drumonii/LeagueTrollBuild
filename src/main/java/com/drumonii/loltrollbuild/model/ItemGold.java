package com.drumonii.loltrollbuild.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

/**
 * Gold information of an {@link Item}.
 */
@Entity
@Table(name = "ITEM_GOLD")
@NoArgsConstructor
@AllArgsConstructor
public class ItemGold {

	@Id
	@GeneratedValue(generator = "generator")
	@GenericGenerator(name = "generator", strategy = "foreign",
			parameters = @Parameter(name = "property", value = "item"))
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

	@OneToOne(optional = false, mappedBy = "image")
	@JsonBackReference
	@Getter @Setter private Item item;

}
