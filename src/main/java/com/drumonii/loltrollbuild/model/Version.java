package com.drumonii.loltrollbuild.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * League of Legend Version (patch).
 *
 * @see <a href="http://leagueoflegends.wikia.com/wiki/Patch">Patch</a>
 */
@Entity
@Table(name = "VERSION")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Version implements Serializable {

	@Id
	@Column(name = "PATCH", nullable = false)
	@Getter @Setter String patch;

}
