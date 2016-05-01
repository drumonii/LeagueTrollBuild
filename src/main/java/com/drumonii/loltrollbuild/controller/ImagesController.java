package com.drumonii.loltrollbuild.controller;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.image.Image;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for returning images of a model as a web response body.
 */
@RestController
@RequestMapping("/img")
public class ImagesController {

	@RequestMapping(value = "/summoner-spells/{id}.*", method = RequestMethod.GET)
	public ResponseEntity<byte[]> summonerSpellImg(@PathVariable("id") SummonerSpell summonerSpell) {
		byte[] imgSrc = summonerSpell.getImage().getImgSrc();
		return ResponseEntity.ok()
				.contentLength(imgSrc.length)
				.contentType(MediaType.parseMediaType(createMediaType(summonerSpell.getImage())))
				.body(imgSrc);
	}

	@RequestMapping(value = "/items/{id}.*", method = RequestMethod.GET)
	public ResponseEntity<byte[]> itemImg(@PathVariable("id") Item item) {
		byte[] imgSrc = item.getImage().getImgSrc();
		return ResponseEntity.ok()
				.contentLength(imgSrc.length)
				.contentType(MediaType.parseMediaType(createMediaType(item.getImage())))
				.body(imgSrc);
	}

	@RequestMapping(value = "/champions/{id}.*", method = RequestMethod.GET)
	public ResponseEntity<byte[]> championImg(@PathVariable("id") Champion champion) {
		byte[] imgSrc = champion.getImage().getImgSrc();
		return ResponseEntity.ok()
				.contentLength(imgSrc.length)
				.contentType(MediaType.parseMediaType(createMediaType(champion.getImage())))
				.body(imgSrc);
	}

	@RequestMapping(value = "/maps/map{id}.*", method = RequestMethod.GET)
	public ResponseEntity<byte[]> mapImg(@PathVariable("id") GameMap map) {
		byte[] imgSrc = map.getImage().getImgSrc();
		return ResponseEntity.ok()
				.contentLength(imgSrc.length)
				.contentType(MediaType.parseMediaType(createMediaType(map.getImage())))
				.body(imgSrc);
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
