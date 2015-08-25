package com.drumonii.loltrollbuild.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Gold information of an {@link Item}.
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ItemGold {

	@JsonProperty("base")
	@Getter @Setter private int base;

	@JsonProperty("total")
	@Getter @Setter private int total;

	@JsonProperty("sell")
	@Getter @Setter private int sell;

	@JsonProperty("purchasable")
	@Getter @Setter private boolean purchasable;

}
