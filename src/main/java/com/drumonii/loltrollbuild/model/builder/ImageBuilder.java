package com.drumonii.loltrollbuild.model.builder;

import com.drumonii.loltrollbuild.model.image.Image;

/**
 * Builder for {@link Image}s.
 *
 * @param <B> the builder type
 */
@SuppressWarnings("unchecked")
public abstract class ImageBuilder<B extends ImageBuilder<B>> {

	protected String full;
	protected String sprite;
	protected String group;
	protected int x;
	protected int y;
	protected int w;
	protected int h;

	public B withFull(String full) {
		this.full = full;
		return (B) this;
	}

	public B withSprite(String sprite) {
		this.sprite = sprite;
		return (B) this;
	}

	public B withGroup(String group) {
		this.group = group;
		return (B) this;
	}

	public B withX(int x) {
		this.x = x;
		return (B) this;
	}

	public B withY(int y) {
		this.y = y;
		return (B) this;
	}

	public B withW(int w) {
		this.w = w;
		return (B) this;
	}

	public B withH(int h) {
		this.h = h;
		return (B) this;
	}

	public Image build() {
		Image image = new Image();
		image.setFull(full);
		image.setSprite(sprite);
		image.setGroup(group);
		image.setX(x);
		image.setY(y);
		image.setW(w);
		image.setH(h);
		return image;
	}

}
