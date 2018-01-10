package com.drumonii.loltrollbuild.model.builder;

import com.drumonii.loltrollbuild.model.image.ItemImage;

/**
 * Builder for {@link ItemImage}s.
 */
public final class ItemImageBuilder extends ImageBuilder<ItemImageBuilder> {

	public ItemImage build() {
		ItemImage itemImage = new ItemImage();
		itemImage.setFull(full);
		itemImage.setSprite(sprite);
		itemImage.setGroup(group);
		itemImage.setX(x);
		itemImage.setY(y);
		itemImage.setW(w);
		itemImage.setH(h);
		return itemImage;
	}

}
