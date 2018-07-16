package com.drumonii.loltrollbuild.model.image;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Objects;

/**
 * Image of a model.
 */
@MappedSuperclass
public class Image implements Serializable {

	@Column(name = "IMG_FULL", unique = true, nullable = false)
	@JsonProperty("full")
	private String full;

	@Column(name = "SPRITE", nullable = false)
	@JsonProperty("sprite")
	private String sprite;

	@Column(name = "IMG_GROUP", nullable = false)
	@JsonProperty("group")
	private String group;

	@Column(name = "IMG_SRC", columnDefinition = "BYTEA", nullable = false)
	@JsonProperty("imgSrc")
	private byte[] imgSrc = new byte[0];

	@Column(name = "X", nullable = false)
	@JsonProperty("x")
	private int x;

	@Column(name = "Y", nullable = false)
	@JsonProperty("y")
	private int y;

	@Column(name = "W", nullable = false)
	@JsonProperty("w")
	private int w;

	@Column(name = "H", nullable = false)
	@JsonProperty("h")
	private int h;

	public Image() {}

	public Image(String full, String sprite, String group, byte[] imgSrc, int x, int y, int w, int h) {
		this.full = full;
		this.sprite = sprite;
		this.group = group;
		this.imgSrc = imgSrc;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public String getFull() {
		return full;
	}

	public void setFull(String full) {
		this.full = full;
	}

	public String getSprite() {
		return sprite;
	}

	public void setSprite(String sprite) {
		this.sprite = sprite;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public byte[] getImgSrc() {
		return imgSrc;
	}

	public void setImgSrc(byte[] imgSrc) {
		this.imgSrc = imgSrc;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Image image = (Image) o;
		return x == image.x &&
				y == image.y &&
				w == image.w &&
				h == image.h &&
				Objects.equals(full, image.full) &&
				Objects.equals(sprite, image.sprite) &&
				Objects.equals(group, image.group);
	}

	@Override
	public int hashCode() {
		return Objects.hash(full, sprite, group, x, y, w, h);
	}

	@Override
	public String toString() {
		return "Image{" +
				"full='" + full + '\'' +
				", sprite='" + sprite + '\'' +
				", group='" + group + '\'' +
				", x=" + x +
				", y=" + y +
				", w=" + w +
				", h=" + h +
				'}';
	}

}
