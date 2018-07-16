package com.drumonii.loltrollbuild.model.image;

import com.drumonii.loltrollbuild.model.GameMap;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Specific {@link Image} of a {@link GameMap} which maps a {@link OneToOne} relationship.
 */
@Entity
@Table(name = "MAP_IMAGE")
public class GameMapImage extends Image implements Serializable {

	@Id
	@Column(name = "MAP_ID", unique = true, nullable = false)
	@JsonIgnore
	private int id;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@PrimaryKeyJoinColumn
	@JsonBackReference
	private GameMap map;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public GameMap getMap() {
		return map;
	}

	public void setMap(GameMap map) {
		this.map = map;
	}

}
