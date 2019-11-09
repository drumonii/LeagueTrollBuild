package com.drumonii.loltrollbuild.api;

import com.drumonii.loltrollbuild.api.service.ImageApiService;
import com.drumonii.loltrollbuild.model.image.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * REST controller for returning images of a model as a cacheable web response body.
 */
@RestController
@RequestMapping("${api.base-path}/img")
public class ImagesRestController {

	@Autowired
	private ImageApiService imageApiService;

	@GetMapping("/summoner-spells/{id}")
	public ResponseEntity<byte[]> summonerSpellImg(@PathVariable("id") int summonerSpellId) {
        return createResponseEntity(imageApiService.getSummonerSpellImage(summonerSpellId));
	}

	@GetMapping("/items/{id}")
	public ResponseEntity<byte[]> itemImg(@PathVariable("id") int itemId) {
        return createResponseEntity(imageApiService.getItemImage(itemId));
	}

	@GetMapping("/champions/{id}")
	public ResponseEntity<byte[]> championImg(@PathVariable("id") int championId) {
		return createResponseEntity(imageApiService.getChampionImage(championId));
	}

	@GetMapping("/champions/{id}/spell/{spellKey}")
	public ResponseEntity<byte[]> championSpellImg(@PathVariable("id") int championId, @PathVariable String spellKey) {
        return createResponseEntity(imageApiService.getChampionSpellImage(championId, spellKey));
    }

	@GetMapping("/champions/{id}/passive")
	public ResponseEntity<byte[]> championPassiveImg(@PathVariable("id") int championId) {
        return createResponseEntity(imageApiService.getChampionPassiveImage(championId));
	}

	@GetMapping("/maps/{mapId}")
	public ResponseEntity<byte[]> mapImg(@PathVariable int mapId) {
        return createResponseEntity(imageApiService.getMapImage(mapId));
	}

    /**
     * Creates the {@link ResponseEntity} of byte array with http status 200 from the {@link Image} if not null,
     * otherwise http status 404.
     *
     * @param image the {@link Image}
     * @return the {@link ResponseEntity} of byte array
     */
    private ResponseEntity<byte[]> createResponseEntity(Image image) {
    	return Optional.ofNullable(image)
				.map(img -> ResponseEntity.ok()
						.contentLength(image.getImgSrc().length)
						.contentType(createMediaType(image))
						.cacheControl(CacheControl.maxAge(365, TimeUnit.DAYS))
						.body(image.getImgSrc()))
				.orElseGet(() -> ResponseEntity.notFound().build());
    }

	/**
	 * Creates a {@link MediaType} from an {@link Image} and its file extension.
	 *
	 * @param image the {@link Image}
	 * @return the {@link MediaType}
	 */
	private MediaType createMediaType(Image image) {
		return new MediaType("image", getFileExtension(image.getFull()));
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

}
