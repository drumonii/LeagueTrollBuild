package com.drumonii.loltrollbuild.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Saved Troll Build.
 */
@Entity
@Table(name = "BUILD")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = { "id", "createdDate", "champion", "item1", "item2", "item3", "item4", "item5", "item6",
        "summonerSpell1", "summonerSpell2", "trinket", "map" })
@ToString(exclude = { "champion", "item1", "item2", "item3", "item4", "item5", "item6", "summonerSpell1",
        "summonerSpell2", "trinket", "map" })
public class Build implements Serializable, Validator {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BUILD_SEQ")
    @SequenceGenerator(name = "BUILD_SEQ", sequenceName = "BUILD_SEQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false)
    @JsonProperty("id")
    @Getter @Setter private int id;

    @NotNull
    @Column(name = "CHAMPION_ID", nullable = false)
    @JsonProperty("champion")
    @Getter @Setter private Integer championId;

    @NotNull
    @Column(name = "ITEM_1_ID", nullable = false)
    @JsonProperty("item1")
    @Getter @Setter private Integer item1Id;

    @NotNull
    @Column(name = "ITEM_2_ID", nullable = false)
    @JsonProperty("item2")
    @Getter @Setter private Integer item2Id;

    @NotNull
    @Column(name = "ITEM_3_ID", nullable = false)
    @JsonProperty("item3")
    @Getter @Setter private Integer item3Id;

    @NotNull
    @Column(name = "ITEM_4_ID", nullable = false)
    @JsonProperty("item4")
    @Getter @Setter private Integer item4Id;

    @NotNull
    @Column(name = "ITEM_5_ID", nullable = false)
    @JsonProperty("item5")
    @Getter @Setter private Integer item5Id;

    @NotNull
    @Column(name = "ITEM_6_ID", nullable = false)
    @JsonProperty("item6")
    @Getter @Setter private Integer item6Id;

    @NotNull
    @Column(name = "SUMMONER_SPELL_1_ID", nullable = false)
    @JsonProperty("summonerSpell1")
    @Getter @Setter private Integer summonerSpell1Id;

    @NotNull
    @Column(name = "SUMMONER_SPELL_2_ID", nullable = false)
    @JsonProperty("summonerSpell2")
    @Getter @Setter private Integer summonerSpell2Id;

    @NotNull
    @Column(name = "TRINKET_ID")
    @JsonProperty("trinket")
    @Getter @Setter private Integer trinketId;

    @NotNull
    @Column(name = "MAP_ID", nullable = false)
    @JsonProperty("map")
    @Getter @Setter private Integer mapId;

    @Column(name = "CREATED_DATE", nullable = false)
    @CreatedDate
    @Getter @Setter private Date createdDate;

    @Transient
    @JsonIgnore
    @Getter @Setter private Champion champion;

    @Transient
    @JsonIgnore
    @Getter @Setter private Item item1;

    @Transient
    @JsonIgnore
    @Getter @Setter private Item item2;

    @Transient
    @JsonIgnore
    @Getter @Setter private Item item3;

    @Transient
    @JsonIgnore
    @Getter @Setter private Item item4;

    @Transient
    @JsonIgnore
    @Getter @Setter private Item item5;

    @Transient
    @JsonIgnore
    @Getter @Setter private Item item6;

    @Transient
    @JsonIgnore
    @Getter @Setter private SummonerSpell summonerSpell1;

    @Transient
    @JsonIgnore
    @Getter @Setter private SummonerSpell summonerSpell2;

    @Transient
    @JsonIgnore
    @Getter @Setter private Item trinket;

    @Transient
    @JsonIgnore
    @Getter @Setter private GameMap map;

    @Override
    public boolean supports(Class<?> clazz) {
        return Build.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Build build = (Build) target;

        if (build.getChampion() == null) {
            errors.rejectValue("champion", "NotNull.build.champion");
        }
        if (build.getItem1() == null) {
            errors.rejectValue("item1", "NotNull.build.item1");
        }
        if (build.getItem2() == null) {
            errors.rejectValue("item2", "NotNull.build.item2");
        }
        if (build.getItem3() == null) {
            errors.rejectValue("item3", "NotNull.build.item3");
        }
        if (build.getItem4() == null) {
            errors.rejectValue("item4", "NotNull.build.item4");
        }
        if (build.getItem5() == null) {
            errors.rejectValue("item5", "NotNull.build.item5");
        }
        if (build.getItem6() == null) {
            errors.rejectValue("item6", "NotNull.build.item6");
        }
        if (build.getSummonerSpell1() == null) {
            errors.rejectValue("summonerSpell1", "NotNull.build.summonerSpell1");
        }
        if (build.getSummonerSpell2() == null) {
            errors.rejectValue("summonerSpell2", "NotNull.build.summonerSpell2");
        }
        if (build.getTrinket() == null) {
            errors.rejectValue("trinket", "NotNull.build.trinket");
        }
        if (build.getMap() == null) {
            errors.rejectValue("map", "NotNull.build.map");
        }
    }

}
