package com.drumonii.loltrollbuild.model.image;

import com.drumonii.loltrollbuild.model.GameMap;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Specific {@link Image} of a {@link GameMap} which maps a {@link OneToOne} relationship.
 */
@Entity
@Table(name = "MAP_IMAGE")
@NoArgsConstructor
@AllArgsConstructor
public class GameMapImage extends Image implements Serializable {

	@Id
	@Column(name = "MAP_ID", unique = true, nullable = false)
	@JsonIgnore
	@Getter @Setter private int id;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@PrimaryKeyJoinColumn
	@JsonBackReference
	@RestResource(exported = false)
	@Getter @Setter private GameMap map;

}
