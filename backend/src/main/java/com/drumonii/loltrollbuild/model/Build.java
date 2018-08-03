package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.rest.view.ApiViews;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Saved Troll Build.
 */
@Entity
@Table(name = "BUILD")
@EntityListeners(AuditingEntityListener.class)
@JsonInclude(Include.NON_NULL)
public class Build implements Serializable, Validator {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BUILD_SEQ")
    @SequenceGenerator(name = "BUILD_SEQ", sequenceName = "BUILD_SEQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false)
    @JsonProperty("id")
    @JsonView({ ApiViews.LtbApi.class })
    private int id;

    @NotNull
    @Column(name = "CHAMPION_ID", nullable = false)
    @JsonProperty("championId")
    @JsonView({ ApiViews.LtbApi.class })
    private Integer championId;

    @NotNull
    @Column(name = "ITEM_1_ID", nullable = false)
    @JsonProperty("item1Id")
    @JsonView({ ApiViews.LtbApi.class })
    private Integer item1Id;

    @NotNull
    @Column(name = "ITEM_2_ID", nullable = false)
    @JsonProperty("item2Id")
    @JsonView({ ApiViews.LtbApi.class })
    private Integer item2Id;

    @NotNull
    @Column(name = "ITEM_3_ID", nullable = false)
    @JsonProperty("item3Id")
    @JsonView({ ApiViews.LtbApi.class })
    private Integer item3Id;

    @NotNull
    @Column(name = "ITEM_4_ID", nullable = false)
    @JsonProperty("item4Id")
    @JsonView({ ApiViews.LtbApi.class })
    private Integer item4Id;

    @NotNull
    @Column(name = "ITEM_5_ID", nullable = false)
    @JsonProperty("item5Id")
    @JsonView({ ApiViews.LtbApi.class })
    private Integer item5Id;

    @NotNull
    @Column(name = "ITEM_6_ID", nullable = false)
    @JsonProperty("item6Id")
    @JsonView({ ApiViews.LtbApi.class })
    private Integer item6Id;

    @NotNull
    @Column(name = "SUMMONER_SPELL_1_ID", nullable = false)
    @JsonProperty("summonerSpell1Id")
    @JsonView({ ApiViews.LtbApi.class })
    private Integer summonerSpell1Id;

    @NotNull
    @Column(name = "SUMMONER_SPELL_2_ID", nullable = false)
    @JsonProperty("summonerSpell2Id")
    @JsonView({ ApiViews.LtbApi.class })
    private Integer summonerSpell2Id;

    @NotNull
    @Column(name = "TRINKET_ID")
    @JsonProperty("trinketId")
    @JsonView({ ApiViews.LtbApi.class })
    private Integer trinketId;

    @NotNull
    @Column(name = "MAP_ID", nullable = false)
    @JsonProperty("mapId")
    @JsonView({ ApiViews.LtbApi.class })
    private Integer mapId;

    @Column(name = "CREATED_DATE", nullable = false)
    @CreatedDate
    @JsonProperty("createdDate")
    @JsonView({ ApiViews.LtbApi.class })
    private LocalDateTime createdDate;

    @Transient
	@JsonProperty("champion")
    @JsonView({ ApiViews.LtbApi.class })
    private Champion champion;

    @Transient
	@JsonProperty("item1")
    @JsonView({ ApiViews.LtbApi.class })
    private Item item1;

    @Transient
	@JsonProperty("item2")
    @JsonView({ ApiViews.LtbApi.class })
    private Item item2;

    @Transient
	@JsonProperty("item3")
    @JsonView({ ApiViews.LtbApi.class })
    private Item item3;

    @Transient
	@JsonProperty("item4")
    @JsonView({ ApiViews.LtbApi.class })
    private Item item4;

    @Transient
	@JsonProperty("item5")
    @JsonView({ ApiViews.LtbApi.class })
    private Item item5;

    @Transient
	@JsonProperty("item6")
    @JsonView({ ApiViews.LtbApi.class })
    private Item item6;

    @Transient
	@JsonProperty("summonerSpell1")
    @JsonView({ ApiViews.LtbApi.class })
    private SummonerSpell summonerSpell1;

    @Transient
	@JsonProperty("summonerSpell2")
    @JsonView({ ApiViews.LtbApi.class })
    private SummonerSpell summonerSpell2;

    @Transient
	@JsonProperty("trinket")
    @JsonView({ ApiViews.LtbApi.class })
    private Item trinket;

    @Transient
	@JsonProperty("map")
    @JsonView({ ApiViews.LtbApi.class })
    private GameMap map;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getChampionId() {
        return championId;
    }

    public void setChampionId(Integer championId) {
        this.championId = championId;
    }

    public Integer getItem1Id() {
        return item1Id;
    }

    public void setItem1Id(Integer item1Id) {
        this.item1Id = item1Id;
    }

    public Integer getItem2Id() {
        return item2Id;
    }

    public void setItem2Id(Integer item2Id) {
        this.item2Id = item2Id;
    }

    public Integer getItem3Id() {
        return item3Id;
    }

    public void setItem3Id(Integer item3Id) {
        this.item3Id = item3Id;
    }

    public Integer getItem4Id() {
        return item4Id;
    }

    public void setItem4Id(Integer item4Id) {
        this.item4Id = item4Id;
    }

    public Integer getItem5Id() {
        return item5Id;
    }

    public void setItem5Id(Integer item5Id) {
        this.item5Id = item5Id;
    }

    public Integer getItem6Id() {
        return item6Id;
    }

    public void setItem6Id(Integer item6Id) {
        this.item6Id = item6Id;
    }

    public Integer getSummonerSpell1Id() {
        return summonerSpell1Id;
    }

    public void setSummonerSpell1Id(Integer summonerSpell1Id) {
        this.summonerSpell1Id = summonerSpell1Id;
    }

    public Integer getSummonerSpell2Id() {
        return summonerSpell2Id;
    }

    public void setSummonerSpell2Id(Integer summonerSpell2Id) {
        this.summonerSpell2Id = summonerSpell2Id;
    }

    public Integer getTrinketId() {
        return trinketId;
    }

    public void setTrinketId(Integer trinketId) {
        this.trinketId = trinketId;
    }

    public Integer getMapId() {
        return mapId;
    }

    public void setMapId(Integer mapId) {
        this.mapId = mapId;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Champion getChampion() {
        return champion;
    }

    public void setChampion(Champion champion) {
        this.champion = champion;
    }

    public Item getItem1() {
        return item1;
    }

    public void setItem1(Item item1) {
        this.item1 = item1;
    }

    public Item getItem2() {
        return item2;
    }

    public void setItem2(Item item2) {
        this.item2 = item2;
    }

    public Item getItem3() {
        return item3;
    }

    public void setItem3(Item item3) {
        this.item3 = item3;
    }

    public Item getItem4() {
        return item4;
    }

    public void setItem4(Item item4) {
        this.item4 = item4;
    }

    public Item getItem5() {
        return item5;
    }

    public void setItem5(Item item5) {
        this.item5 = item5;
    }

    public Item getItem6() {
        return item6;
    }

    public void setItem6(Item item6) {
        this.item6 = item6;
    }

    public SummonerSpell getSummonerSpell1() {
        return summonerSpell1;
    }

    public void setSummonerSpell1(SummonerSpell summonerSpell1) {
        this.summonerSpell1 = summonerSpell1;
    }

    public SummonerSpell getSummonerSpell2() {
        return summonerSpell2;
    }

    public void setSummonerSpell2(SummonerSpell summonerSpell2) {
        this.summonerSpell2 = summonerSpell2;
    }

    public Item getTrinket() {
        return trinket;
    }

    public void setTrinket(Item trinket) {
        this.trinket = trinket;
    }

    public GameMap getMap() {
        return map;
    }

    public void setMap(GameMap map) {
        this.map = map;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Build build = (Build) o;
        return Objects.equals(championId, build.championId) &&
                Objects.equals(item1Id, build.item1Id) &&
                Objects.equals(item2Id, build.item2Id) &&
                Objects.equals(item3Id, build.item3Id) &&
                Objects.equals(item4Id, build.item4Id) &&
                Objects.equals(item5Id, build.item5Id) &&
                Objects.equals(item6Id, build.item6Id) &&
                Objects.equals(summonerSpell1Id, build.summonerSpell1Id) &&
                Objects.equals(summonerSpell2Id, build.summonerSpell2Id) &&
                Objects.equals(trinketId, build.trinketId) &&
                Objects.equals(mapId, build.mapId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(championId, item1Id, item2Id, item3Id, item4Id, item5Id, item6Id, summonerSpell1Id, summonerSpell2Id, trinketId, mapId);
    }

    @Override
    public String toString() {
        return "Build{" +
                "id=" + id +
                ", championId=" + championId +
                ", item1Id=" + item1Id +
                ", item2Id=" + item2Id +
                ", item3Id=" + item3Id +
                ", item4Id=" + item4Id +
                ", item5Id=" + item5Id +
                ", item6Id=" + item6Id +
                ", summonerSpell1Id=" + summonerSpell1Id +
                ", summonerSpell2Id=" + summonerSpell2Id +
                ", trinketId=" + trinketId +
                ", mapId=" + mapId +
                ", createdDate=" + createdDate +
                '}';
    }

}
