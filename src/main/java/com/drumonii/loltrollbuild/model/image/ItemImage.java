package com.drumonii.loltrollbuild.model.image;

import com.drumonii.loltrollbuild.model.Item;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

/**
 * Specific {@link Image} of a {@link Item} which maps a {@link OneToOne} relationship.
 */
@Entity
@Table(name = "ITEM_IMAGE")
@NoArgsConstructor
@AllArgsConstructor
public class ItemImage extends Image {

	public ItemImage(String full, String sprite, String group, int x, int y, int w, int h) {
		super(full, sprite, group, x, y, w, h);
	}

	@Id
	@GeneratedValue(generator = "generator")
	@GenericGenerator(name = "generator", strategy = "foreign",
			parameters = @Parameter(name = "property", value = "item"))
	@Column(name = "ITEM_ID", unique = true, nullable = false)
	@JsonIgnore
	@Getter @Setter private int id;

	@OneToOne(optional = false, mappedBy = "image")
	@JsonBackReference
	@Getter @Setter private Item item;

}
