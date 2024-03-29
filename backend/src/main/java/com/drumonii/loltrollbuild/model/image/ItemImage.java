package com.drumonii.loltrollbuild.model.image;

import com.drumonii.loltrollbuild.model.Item;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Specific {@link Image} of a {@link Item} which maps a {@link OneToOne} relationship.
 */
@Entity
@Table(name = "ITEM_IMAGE")
public class ItemImage extends Image implements Serializable {

	@Id
	@Column(name = "ITEM_ID", unique = true, nullable = false)
	@JsonIgnore
	private int id;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@PrimaryKeyJoinColumn
	@JsonBackReference
	private Item item;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
		item.setId(id);
	}

	@Override
	public void setFull(String full) {
		super.setFull(full);
		setId(Integer.parseInt(full.substring(0, full.lastIndexOf('.'))));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		ItemImage itemImage = (ItemImage) o;
		return id == itemImage.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), id);
	}

}
