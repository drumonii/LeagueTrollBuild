package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.model.*;
import com.drumonii.loltrollbuild.model.image.Image;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * REST controller for returning images of a model as a cacheable web response body.
 */
@RestController
@RequestMapping("/img")
public class ImagesRestController {

	@GetMapping(value = "/summoner-spells/{id}.*")
	public ResponseEntity<byte[]> summonerSpellImg(@PathVariable("id") SummonerSpell summonerSpell) {
		if (summonerSpell == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok()
				.contentLength(summonerSpell.getImage().getImgSrc().length)
				.contentType(createMediaType(summonerSpell.getImage()))
				.cacheControl(CacheControl.maxAge(365, TimeUnit.DAYS))
				.lastModified(getLastModified(summonerSpell.getLastModifiedDate()))
				.body(summonerSpell.getImage().getImgSrc());
	}

	@GetMapping(value = "/items/{id}.*")
	public ResponseEntity<byte[]> itemImg(@PathVariable("id") Item item) {
		if (item == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok()
				.contentLength(item.getImage().getImgSrc().length)
				.contentType(createMediaType(item.getImage()))
				.cacheControl(CacheControl.maxAge(365, TimeUnit.DAYS))
				.lastModified(getLastModified(item.getLastModifiedDate()))
				.body(item.getImage().getImgSrc());
	}

	@GetMapping(value = "/champions/{id}.*")
	public ResponseEntity<byte[]> championImg(@PathVariable("id") Champion champion) {
		if (champion == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok()
				.contentLength(champion.getImage().getImgSrc().length)
				.contentType(createMediaType(champion.getImage()))
				.cacheControl(CacheControl.maxAge(365, TimeUnit.DAYS))
				.lastModified(getLastModified(champion.getLastModifiedDate()))
				.body(champion.getImage().getImgSrc());
	}

	@GetMapping(value = "/champions/{id}/spell/{spellKey}.*")
	public ResponseEntity<byte[]> championSpellImg(@PathVariable("id") Champion champion, @PathVariable String spellKey) {
		if (champion == null) {
			return ResponseEntity.notFound().build();
		}
		Optional<ChampionSpell> spell = champion.getSpells().stream()
				.filter(s -> spellKey.equals(s.getKey()))
				.findFirst();
		return spell.map(championSpell -> ResponseEntity.ok()
				.contentLength(championSpell.getImage().getImgSrc().length)
				.contentType(createMediaType(championSpell.getImage()))
				.cacheControl(CacheControl.maxAge(365, TimeUnit.DAYS))
				.lastModified(getLastModified(champion.getLastModifiedDate()))
				.body(championSpell.getImage().getImgSrc())).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping(value = "/champions/{id}/passive/{passive}.*")
	public ResponseEntity<byte[]> championPassiveImg(@PathVariable("id") Champion champion) {
		if (champion == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok()
				.contentLength(champion.getPassive().getImage().getImgSrc().length)
				.contentType(createMediaType(champion.getPassive().getImage()))
				.cacheControl(CacheControl.maxAge(365, TimeUnit.DAYS))
				.lastModified(getLastModified(champion.getLastModifiedDate()))
				.body(champion.getPassive().getImage().getImgSrc());
	}

	@GetMapping(value = "/maps/map{id}.*")
	public ResponseEntity<byte[]> mapImg(@PathVariable("id") GameMap gameMap) {
		if (gameMap == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok()
				.contentLength(gameMap.getImage().getImgSrc().length)
				.contentType(createMediaType(gameMap.getImage()))
				.cacheControl(CacheControl.maxAge(365, TimeUnit.DAYS))
				.lastModified(getLastModified(gameMap.getLastModifiedDate()))
				.body(gameMap.getImage().getImgSrc());
	}

	/**
	 * Creates a {@link MediaType} from an {@link Image} and its file extension.
	 *
	 * @param image the {@link Image}
	 * @return the {@link MediaType}
	 */
	private MediaType createMediaType(Image image) {
		return MediaType.parseMediaType("image/" + getFileExtension(image.getFull()));
	}

	/**
	 * Gets the file extension from the filename.
	 *
	 * @param filename the filename
	 * @return the file extension
	 */
	private String getFileExtension(String filename) {
		return filename.substring(filename.lastIndexOf('.') + 1);
	}

	/**
	 * Gets the last modified time in milliseconds from the last modified date.
	 *
	 * @param lastModifiedDate the last modified date
	 * @return the last modified time in milliseconds
	 */
	private long getLastModified(LocalDateTime lastModifiedDate) {
		return lastModifiedDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}

}
