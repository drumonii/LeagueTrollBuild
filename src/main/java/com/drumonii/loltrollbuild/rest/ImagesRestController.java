package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.image.Image;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * REST controller for returning images of a model as a web response body.
 */
@RestController
@RequestMapping("/img")
public class ImagesRestController {

	@RequestMapping(value = "/summoner-spells/{id}.*", method = RequestMethod.GET)
	public ResponseEntity<byte[]> summonerSpellImg(@PathVariable("id") SummonerSpell summonerSpell) {
		return ResponseEntity.ok()
				.contentLength(summonerSpell.getImage().getImgSrc().length)
				.contentType(MediaType.parseMediaType(createMediaType(summonerSpell.getImage())))
				.cacheControl(CacheControl.maxAge(31556926, TimeUnit.SECONDS))
				.lastModified(summonerSpell.getLastModifiedDate().getTime())
				.body(summonerSpell.getImage().getImgSrc());
	}

	@RequestMapping(value = "/items/{id}.*", method = RequestMethod.GET)
	public ResponseEntity<byte[]> itemImg(@PathVariable("id") Item item) {
		return ResponseEntity.ok()
				.contentLength(item.getImage().getImgSrc().length)
				.contentType(MediaType.parseMediaType(createMediaType(item.getImage())))
				.cacheControl(CacheControl.maxAge(31556926, TimeUnit.SECONDS))
				.lastModified(item.getLastModifiedDate().getTime())
				.body(item.getImage().getImgSrc());
	}

	@RequestMapping(value = "/champions/{id}.*", method = RequestMethod.GET)
	public ResponseEntity<byte[]> championImg(@PathVariable("id") Champion champion) {
		return ResponseEntity.ok()
				.contentLength(champion.getImage().getImgSrc().length)
				.contentType(MediaType.parseMediaType(createMediaType(champion.getImage())))
				.cacheControl(CacheControl.maxAge(31556926, TimeUnit.SECONDS))
				.lastModified(champion.getLastModifiedDate().getTime())
				.body(champion.getImage().getImgSrc());
	}

	@RequestMapping(value = "/maps/map{id}.*", method = RequestMethod.GET)
	public ResponseEntity<byte[]> mapImg(@PathVariable("id") GameMap gameMap) {
		return ResponseEntity.ok()
				.contentLength(gameMap.getImage().getImgSrc().length)
				.contentType(MediaType.parseMediaType(createMediaType(gameMap.getImage())))
				.cacheControl(CacheControl.maxAge(31556926, TimeUnit.SECONDS))
				.lastModified(gameMap.getLastModifiedDate().getTime())
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
