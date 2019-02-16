const fs = require('fs');
const http = require('http');

/**
 * Gets the `champion.json` file for the given version.
 *
 * @param {string} latestVersion the latest Riot patch version
 */
function getChampions(latestVersion) {
  console.log('getting champion json test data...');

  function endCallback(rawChampionsData) {
    writeFile('champions_data_dragon', rawChampionsData);

    getChampion(latestVersion, rawChampionsData);
  }

  getResponse(`http://ddragon.leagueoflegends.com/cdn/${latestVersion}/data/en_US/champion.json`, endCallback);
}

/**
 * Gets individual the `champion/{championId}.json` files for the given version and raw champion data.
 *
 * @param {string} latestVersion the latest Riot patch version
 * @param {string} rawChampionData the raw champions data (champion.json)
 */
function getChampion(latestVersion, rawChampionData) {
  const championResponse = JSON.parse(rawChampionData);
  for (const champion of Object.keys(championResponse.data)) {
    console.log(`getting champion ${champion} json test data...`);

    function endCallback(rawChampionData) {
      writeFile(`${champion}_data_dragon`, rawChampionData)
    }

    getResponse(`http://ddragon.leagueoflegends.com/cdn/${latestVersion}/data/en_US/champion/${champion}.json`, endCallback);
  }
}

/**
 * Gets the `item.json` file for the given version.
 *
 * @param {string} latestVersion the latest Riot patch version
 */
function getItems(latestVersion) {
  console.log('getting items json test data...');

  function endCallback(rawItemsData) {
    writeFile('items_data_dragon', rawItemsData);
  }

  getResponse(`http://ddragon.leagueoflegends.com/cdn/${latestVersion}/data/en_US/item.json`, endCallback);
}

/**
 * Gets the `map.json` file for the given version.
 *
 * @param {string} latestVersion the latest Riot patch version
 */
function getMaps(latestVersion) {
  console.log('getting maps json test data...');

  function endCallback(rawMapsData) {
    writeFile('maps_data_dragon', rawMapsData);
  }

  getResponse(`http://ddragon.leagueoflegends.com/cdn/${latestVersion}/data/en_US/map.json`, endCallback);
}

/**
 * Gets the `summoner.json` file for the given version.
 *
 * @param {string} latestVersion the latest Riot patch version
 */
function getSummonerSpells(latestVersion) {
  console.log('getting summoner spells json test data...');

  function endCallback(rawSummonerSpellsData) {
    writeFile('summoners_data_dragon', rawSummonerSpellsData);
  }

  getResponse(`http://ddragon.leagueoflegends.com/cdn/${latestVersion}/data/en_US/summoner.json`, endCallback);
}

/**
 * Writes the file using the given file name and raw data.
 *
 * @param {string} fileName the json file name
 * @param {string} rawData the raw data
 */
function writeFile(fileName, rawData) {
  const jsonFileName = `${fileName}.json`;
  console.log(`writing ${jsonFileName}...`);
  fs.writeFile(`./src/test/resources/${jsonFileName}`, rawData, (err) => {
    if (err) {
      console.error(err.message);
    }
    console.log(`${jsonFileName} has been saved!`);
  });
}

/**
 * Gets the HTTP get response.
 *
 * @param {string} httpUrl the HTTP url
 * @param {Function} endCallback the on end callback
 */
function getResponse(httpUrl, endCallback) {
  http.get(httpUrl, (res) => {
    const {statusCode} = res;

    let error;
    if (statusCode !== 200) {
      error = new Error(`Request Failed. Status Code: ${statusCode}. Try again later`);
    }

    if (error) {
      console.error(error.message);
      // consume response data to free up memory
      res.resume();
      return;
    }

    res.setEncoding('utf8');
    let rawData = '';
    res.on('data', (chunk) => {
      rawData += chunk;
    });
    res.on('end', () => endCallback(rawData));
  });
}

/**
 * Fetches all test data from Riot's ddragon API.
 */
function getTestData() {
  console.log('getting latest json test data from ddragon...\n');

  console.log('getting versions json test data...');

  function endCallback(rawVersionsData) {
    const versions = JSON.parse(rawVersionsData);

    const latestVersion = versions[0];
    console.log(`found latest version to be '${latestVersion}'`);

    writeFile('versions_data_dragon', rawVersionsData);

    // Get rest of the test data
    getChampions(latestVersion);
    getItems(latestVersion);
    getMaps(latestVersion);
    getSummonerSpells(latestVersion);
  }

  getResponse('http://ddragon.leagueoflegends.com/api/versions.json', endCallback);
}

getTestData();
