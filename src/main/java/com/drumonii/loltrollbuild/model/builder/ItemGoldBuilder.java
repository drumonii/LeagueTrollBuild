package com.drumonii.loltrollbuild.model.builder;

import com.drumonii.loltrollbuild.model.ItemGold;

/**
 * Builder for {@link ItemGold}s.
 */
public final class ItemGoldBuilder {

	private int base;
	private int total;
	private int sell;
	private boolean purchasable;

	public ItemGoldBuilder withBase(int base) {
		this.base = base;
		return this;
	}

	public ItemGoldBuilder withTotal(int total) {
		this.total = total;
		return this;
	}

	public ItemGoldBuilder withSell(int sell) {
		this.sell = sell;
		return this;
	}

	public ItemGoldBuilder withPurchasable(boolean purchasable) {
		this.purchasable = purchasable;
		return this;
	}

	public ItemGold build() {
		ItemGold itemGold = new ItemGold();
		itemGold.setBase(base);
		itemGold.setTotal(total);
		itemGold.setSell(sell);
		itemGold.setPurchasable(purchasable);
		return itemGold;
	}

}
