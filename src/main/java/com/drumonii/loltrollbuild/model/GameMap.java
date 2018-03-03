package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.model.image.GameMapImage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.persistence.Version;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * League of Legends Map. Also called a Field of Justice. Expressed as a "GameMap" to avoid the name space with a
 * {@link Map}.
 *
 * @see <a href="http://leagueoflegends.wikia.com/wiki/Field_of_Justice">Field of Justice Wiki</a>
 */
@Entity
@Table(name = "MAP")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = { "version", "lastModifiedDate" })
@ToString
public class GameMap implements Serializable {

	@Id
	@Column(name = "ID", unique = true, nullable = false)
	@JsonProperty("mapId")
	@Getter @Setter private int mapId;

	@Version
	@Column(name = "VERSION", nullable = false)
	@JsonIgnore
	@Getter @Setter private Long version;

	@Column(name = "NAME", nullable = false)
	@JsonProperty("mapName")
	@Getter @Setter private String mapName;

	@Column(name = "LAST_MODIFIED_DATE", nullable = false)
	@LastModifiedDate
	@JsonIgnore
	@Getter @Setter private LocalDateTime lastModifiedDate;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "map")
	@JsonManagedReference
	@JsonProperty("image")
	@Getter @Setter private GameMapImage image;

}
