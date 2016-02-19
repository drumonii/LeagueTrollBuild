package com.drumonii.loltrollbuild.model.image;

import com.drumonii.loltrollbuild.model.GameMap;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Specific {@link Image} of a {@link GameMap} which maps a {@link OneToOne} relationship.
 */
@Entity
@Table(name = "MAP_IMAGE")
@NoArgsConstructor
@AllArgsConstructor
public class GameMapImage extends Image {

	public GameMapImage(String full, String sprite, String group, byte[] imgSrc, int x, int y, int w, int h) {
		super(full, sprite, group, imgSrc, x, y, w, h);
	}

	@Id
	@Column(name = "MAP_ID", unique = true, nullable = false)
	@JsonIgnore
	@Getter @Setter private int id;

	@MapsId
	@OneToOne(optional = false, mappedBy = "image")
	@JsonBackReference
	@Getter @Setter private GameMap map;

}
