package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.model.image.SummonerSpellImage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * League of Legend Summoner Spell.
 *
 * @see <a href="http://leagueoflegends.wikia.com/wiki/Summoner">Summoner Spell Wiki</a>
 */
@Entity
@Table(name = "SUMMONER_SPELL")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "lastModifiedDate")
@ToString
public class SummonerSpell implements Serializable {

	@Id
	@Column(name = "ID", unique = true, nullable = false)
	@JsonProperty("id")
	@Getter @Setter private int id;

	@Column(name = "NAME", nullable = false)
	@JsonProperty("name")
	@Getter @Setter private String name;

	@Column(name = "DESCRIPTION", nullable = false)
	@JsonProperty("description")
	@Getter @Setter private String description;

	@Column(name = "LAST_MODIFIED_DATE", nullable = false)
	@LastModifiedDate
	@JsonIgnore
	@Getter @Setter private Date lastModifiedDate;

	@OneToOne(cascade = CascadeType.ALL, optional = false)
	@PrimaryKeyJoinColumn
	@JsonManagedReference
	@JsonProperty("image")
	@Getter @Setter private SummonerSpellImage image;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "SUMMONER_SPELL_COOLDOWN", joinColumns = @JoinColumn(name = "SUMMONER_SPELL_ID"))
	@Column(name = "COOLDOWN")
	@JsonProperty("cooldown")
	@Getter @Setter private Set<Integer> cooldown;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "SUMMONER_SPELL_MODE", joinColumns = @JoinColumn(name = "SUMMONER_SPELL_ID"))
	@Enumerated(EnumType.STRING)
	@Column(name = "MODE")
	@JsonProperty("modes")
	@Getter @Setter private Set<GameMode> modes;

	public enum GameMode {

		CLASSIC, ODIN, ARAM, TUTORIAL, ONEFORALL, ASCENSION, FIRSTBLOOD, KINGPORO

	}

}
