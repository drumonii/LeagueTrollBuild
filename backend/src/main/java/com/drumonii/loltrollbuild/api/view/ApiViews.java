package com.drumonii.loltrollbuild.api.view;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * Custom {@link JsonView}s for both Riot's ddragon API and LTB's API.
 */
public interface ApiViews {

    /**
     * Views applicable to both APIs.
     */
    interface AllApis {}

    /**
     * View applicable to only LTB's API.
     */
    interface LtbApi extends AllApis {}

    /**
     * View applicable to only Riot's ddragon API.
     */
    interface RiotDdragonApi extends AllApis {}

}
