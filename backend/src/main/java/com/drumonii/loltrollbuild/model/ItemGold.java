package com.drumonii.loltrollbuild.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Gold information of an {@link Item}.
 */
@Entity
@Table(name = "ITEM_GOLD")
public class ItemGold implements Serializable {

    @Id
    @Column(name = "ITEM_ID", unique = true, nullable = false)
    @JsonIgnore
    private int id;

    @Column(name = "BASE", nullable = false)
    @JsonProperty("base")
    private int base;

    @Column(name = "TOTAL", nullable = false)
    @JsonProperty("total")
    private int total;

    @Column(name = "SELL", nullable = false)
    @JsonProperty("sell")
    private int sell;

    @Column(name = "PURCHASABLE", nullable = false)
    @JsonProperty("purchasable")
    private boolean purchasable;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @PrimaryKeyJoinColumn
    @JsonBackReference
    private Item item;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBase() {
        return base;
    }

    public void setBase(int base) {
        this.base = base;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSell() {
        return sell;
    }

    public void setSell(int sell) {
        this.sell = sell;
    }

    public boolean isPurchasable() {
        return purchasable;
    }

    public void setPurchasable(boolean purchasable) {
        this.purchasable = purchasable;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ItemGold itemGold = (ItemGold) o;
        return base == itemGold.base &&
                total == itemGold.total &&
                sell == itemGold.sell &&
                purchasable == itemGold.purchasable;
    }

    @Override
    public int hashCode() {
        return Objects.hash(base, total, sell, purchasable);
    }

    @Override
    public String toString() {
        return "ItemGold{" +
                "base=" + base +
                ", total=" + total +
                ", sell=" + sell +
                ", purchasable=" + purchasable +
                '}';
    }

}
