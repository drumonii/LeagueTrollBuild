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
@RequestMapping("${api.base-path}/img")
public class ImagesRestController {

	@GetMapping(path = "/summoner-spells/{id}")
	public ResponseEntity<byte[]> summonerSpellImg(@PathVariable("id") SummonerSpell summonerSpell) {
		if (summonerSpell == null) {
			return ResponseEntity.notFound().build();
		}
		return createResponseEntity(summonerSpell.getImage(), summonerSpell.getLastModifiedDate());
	}

	@GetMapping(path = "/items/{id}")
	public ResponseEntity<byte[]> itemImg(@PathVariable("id") Item item) {
		if (item == null) {
			return ResponseEntity.notFound().build();
		}
		return createResponseEntity(item.getImage(), item.getLastModifiedDate());
	}

	@GetMapping(path = "/champions/{id}")
	public ResponseEntity<byte[]> championImg(@PathVariable("id") Champion champion) {
		if (champion == null) {
			return ResponseEntity.notFound().build();
		}
		return createResponseEntity(champion.getImage(), champion.getLastModifiedDate());
	}

	@GetMapping(path = "/champions/{id}/spell/{spellKey}")
	public ResponseEntity<byte[]> championSpellImg(@PathVariable("id") Champion champion, @PathVariable String spellKey) {
		if (champion == null) {
			return ResponseEntity.notFound().build();
		}
		Optional<ChampionSpell> spell = champion.getSpells().stream()
				.filter(s -> spellKey.equals(s.getKey()))
				.findFirst();
		return spell.map(championSpell -> createResponseEntity(championSpell.getImage(), champion.getLastModifiedDate()))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping(path = "/champions/{id}/passive")
	public ResponseEntity<byte[]> championPassiveImg(@PathVariable("id") Champion champion) {
		if (champion == null) {
			return ResponseEntity.notFound().build();
		}
		return createResponseEntity(champion.getPassive().getImage(), champion.getLastModifiedDate());
	}

	@GetMapping(path = "/maps/{mapId}")
	public ResponseEntity<byte[]> mapImg(@PathVariable("mapId") GameMap gameMap) {
		if (gameMap == null) {
			return ResponseEntity.notFound().build();
		}
		return createResponseEntity(gameMap.getImage(), gameMap.getLastModifiedDate());
	}

	/**
	 * Creates the {@link ResponseEntity} of byte array with http status 200 from the {@link Image}.
	 *
	 * @param image the {@link Image}
	 * @param lastModifiedDate the last modified date
	 * @return the {@link ResponseEntity} of byte array
	 */
	private ResponseEntity<byte[]> createResponseEntity(Image image, LocalDateTime lastModifiedDate) {
		return ResponseEntity.ok()
				.contentLength(image.getImgSrc().length)
				.contentType(createMediaType(image))
				.cacheControl(CacheControl.maxAge(365, TimeUnit.DAYS))
				.lastModified(getLastModified(lastModifiedDate))
				.body(image.getImgSrc());
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
