package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.model.image.GameMapImage;
import com.drumonii.loltrollbuild.api.view.ApiViews;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.persistence.Version;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * League of Legends Map. Also called a Field of Justice. Expressed as a "GameMap" to avoid the name space with a
 * {@link Map}.
 *
 * @see <a href="http://leagueoflegends.wikia.com/wiki/Field_of_Justice">Field of Justice Wiki</a>
 */
@Entity
@Table(name = "MAP")
@EntityListeners(AuditingEntityListener.class)
public class GameMap implements Serializable {

	@Id
	@Column(name = "ID", unique = true, nullable = false)
	@JsonProperty("mapId")
	@JsonView({ ApiViews.AllApis.class })
	private int mapId;

	@Version
	@Column(name = "VERSION", nullable = false)
	@JsonIgnore
	private Long version;

	@Column(name = "NAME", nullable = false)
	@JsonProperty("mapName")
	@JsonView({ ApiViews.AllApis.class })
	private String mapName;

	@Column(name = "LAST_MODIFIED_DATE", nullable = false)
	@LastModifiedDate
	@JsonIgnore
	private LocalDateTime lastModifiedDate;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "map")
	@JsonManagedReference
	@JsonProperty("image")
	@JsonView({ ApiViews.RiotApi.class })
	private GameMapImage image;

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public LocalDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public GameMapImage getImage() {
		return image;
	}

	public void setImage(GameMapImage image) {
		this.image = image;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		GameMap gameMap = (GameMap) o;
		return mapId == gameMap.mapId &&
				Objects.equals(mapName, gameMap.mapName) &&
				Objects.equals(image, gameMap.image);
	}

	@Override
	public int hashCode() {
		return Objects.hash(mapId, mapName, image);
	}

	@Override
	public String toString() {
		return "GameMap{" +
				"mapId=" + mapId +
				", version=" + version +
				", mapName='" + mapName + '\'' +
				", lastModifiedDate=" + lastModifiedDate +
				", image=" + image +
				'}';
	}

}
