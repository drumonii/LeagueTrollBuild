package com.drumonii.loltrollbuild.model.image;

import com.drumonii.loltrollbuild.model.Item;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Specific {@link Image} of a {@link Item} which maps a {@link OneToOne} relationship.
 */
@Entity
@Table(name = "ITEM_IMAGE")
@NoArgsConstructor
@AllArgsConstructor
public class ItemImage extends Image implements Serializable {

	@Id
	@Column(name = "ITEM_ID", unique = true, nullable = false)
	@JsonIgnore
	@Getter @Setter private int id;

	@MapsId
	@OneToOne(optional = false, mappedBy = "image")
	@JsonBackReference
	@Getter @Setter private Item item;

}
