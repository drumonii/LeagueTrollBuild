package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.model.*;
import com.drumonii.loltrollbuild.model.image.Image;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
		return ResponseEntity.ok()
				.contentLength(summonerSpell.getImage().getImgSrc().length)
				.contentType(MediaType.parseMediaType(createMediaType(summonerSpell.getImage())))
				.cacheControl(CacheControl.maxAge(31556926, TimeUnit.SECONDS))
				.lastModified(summonerSpell.getLastModifiedDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
				.body(summonerSpell.getImage().getImgSrc());
	}

	@GetMapping(value = "/items/{id}.*")
	public ResponseEntity<byte[]> itemImg(@PathVariable("id") Item item) {
		return ResponseEntity.ok()
				.contentLength(item.getImage().getImgSrc().length)
				.contentType(MediaType.parseMediaType(createMediaType(item.getImage())))
				.cacheControl(CacheControl.maxAge(31556926, TimeUnit.SECONDS))
				.lastModified(item.getLastModifiedDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
				.body(item.getImage().getImgSrc());
	}

	@GetMapping(value = "/champions/{id}.*")
	public ResponseEntity<byte[]> championImg(@PathVariable("id") Champion champion) {
		return ResponseEntity.ok()
				.contentLength(champion.getImage().getImgSrc().length)
				.contentType(MediaType.parseMediaType(createMediaType(champion.getImage())))
				.cacheControl(CacheControl.maxAge(31556926, TimeUnit.SECONDS))
				.lastModified(champion.getLastModifiedDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
				.body(champion.getImage().getImgSrc());
	}

	@GetMapping(value = "/champions/{id}/spell/{spellKey}.*")
	public ResponseEntity<byte[]> championSpellImg(@PathVariable("id") Champion champion,
			@PathVariable String spellKey) {
		Optional<ChampionSpell> spell = champion.getSpells().stream()
				.filter(s -> spellKey.equals(s.getKey()))
				.findFirst();
		if (!spell.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok()
				.contentLength(spell.get().getImage().getImgSrc().length)
				.contentType(MediaType.parseMediaType(createMediaType(spell.get().getImage())))
				.cacheControl(CacheControl.maxAge(31556926, TimeUnit.SECONDS))
				.lastModified(champion.getLastModifiedDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
				.body(spell.get().getImage().getImgSrc());
	}

	@GetMapping(value = "/champions/{id}/passive/{passive}.*")
	public ResponseEntity<byte[]> championPassiveImg(@PathVariable("id") Champion champion) {
		return ResponseEntity.ok()
				.contentLength(champion.getPassive().getImage().getImgSrc().length)
				.contentType(MediaType.parseMediaType(createMediaType(champion.getPassive().getImage())))
				.cacheControl(CacheControl.maxAge(31556926, TimeUnit.SECONDS))
				.lastModified(champion.getLastModifiedDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
				.body(champion.getPassive().getImage().getImgSrc());
	}

	@GetMapping(value = "/maps/map{id}.*")
	public ResponseEntity<byte[]> mapImg(@PathVariable("id") GameMap gameMap) {
		return ResponseEntity.ok()
				.contentLength(gameMap.getImage().getImgSrc().length)
				.contentType(MediaType.parseMediaType(createMediaType(gameMap.getImage())))
				.cacheControl(CacheControl.maxAge(31556926, TimeUnit.SECONDS))
				.lastModified(gameMap.getLastModifiedDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
				.body(gameMap.getImage().getImgSrc());
	}

	/**
	 * Creates a {@link MediaType} from an {@link Image} and its file extension.
	 *
	 * @param image the {@link Image}
	 * @return the {@link MediaType}
	 */
	private String createMediaType(Image image) {
		return "image/" + FilenameUtils.getExtension(image.getFull());
	}

}
