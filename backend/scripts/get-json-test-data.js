const fs = require('fs');
const http = require('http');
const path = require('path');

/**
 * Gets the `champion.json` file for the given version.
 *
 * @param {string} latestVersion the latest Riot patch version
 */
function getChampions(latestVersion) {
  console.log('getting champion json test data...');

  getResponse(`http://ddragon.leagueoflegends.com/cdn/${latestVersion}/data/en_US/champion.json`, (rawChampionsData) => {
    writeFile('champions_data_dragon', rawChampionsData);
    getChampion(latestVersion, rawChampionsData);
  });
}

/**
 * Gets individual the `champion/{championId}.json` files for the given version and raw champion data.
 *
 * @param {string} latestVersion the latest Riot patch version
 * @param {string} rawChampionsData the raw champions data (champion.json)
 */
function getChampion(latestVersion, rawChampionsData) {
  const championResponse = JSON.parse(rawChampionsData);
  Object.keys(championResponse.data).forEach(champion => {
    console.log(`getting champion ${champion} json test data...`);

    getResponse(`http://ddragon.leagueoflegends.com/cdn/${latestVersion}/data/en_US/champion/${champion}.json`,
      (rawChampionData) => writeFile(`${champion}_data_dragon`, rawChampionData));
  });
}

/**
 * Gets the `item.json` file for the given version.
 *
 * @param {string} latestVersion the latest Riot patch version
 */
function getItems(latestVersion) {
  console.log('getting items json test data...');

  getResponse(`http://ddragon.leagueoflegends.com/cdn/${latestVersion}/data/en_US/item.json`,
    (rawItemsData) => writeFile('items_data_dragon', rawItemsData));
}

/**
 * Gets the `map.json` file for the given version.
 *
 * @param {string} latestVersion the latest Riot patch version
 */
function getMaps(latestVersion) {
  console.log('getting maps json test data...');

  getResponse(`http://ddragon.leagueoflegends.com/cdn/${latestVersion}/data/en_US/map.json`,
    (rawMapsData) => writeFile('maps_data_dragon', rawMapsData));
}

/**
 * Gets the `summoner.json` file for the given version.
 *
 * @param {string} latestVersion the latest Riot patch version
 */
function getSummonerSpells(latestVersion) {
  console.log('getting summoner spells json test data...');

  getResponse(`http://ddragon.leagueoflegends.com/cdn/${latestVersion}/data/en_US/summoner.json`,
    (rawSummonerSpellsData) => writeFile('summoners_data_dragon', rawSummonerSpellsData));
}

/**
 * Writes the file using the given file name and raw data.
 *
 * @param {string} fileName the json file name
 * @param {string} rawData the raw data
 */
function writeFile(fileName, rawData) {
  const jsonFileName = `${fileName}.json`;
  const resolvedJsonFilePath = path.resolve(__dirname, `../src/test/resources/${jsonFileName}`);
  console.log(`writing ${jsonFileName} to ${resolvedJsonFilePath}...`);
  fs.writeFile(resolvedJsonFilePath, JSON.stringify(JSON.parse(rawData), null, 2), (err) => {
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
 * Determines if the latest version from Riot is the same as the latest saved version in the test json file.
 * If that's the case, the script will terminate. Only if the `--force` flag is set, then the script continues.
 *
 * @param latestVersion the latest Riot patch version
 */
function checkIsNewVersion(latestVersion) {
  const versionsJsonFile = path.resolve(__dirname, '../src/test/resources/versions_data_dragon.json');

  const savedVersions = fs.readFileSync(versionsJsonFile, 'utf8');
  const savedVersion = JSON.parse(savedVersions)[0];

  if (latestVersion === savedVersion) {
    const arg = process.argv[2] || '';
    if (arg !== '--force') {
      console.log('the latest version from Riot is the same as the latest saved version in the test json file. ' +
        'No writing of test json files will be done. Use --force to ignore');
      process.exit(0);
    }
  }
}

/**
 * Entry point for fetching all test data from Riot's ddragon API.
 */
(function getTestData() {
  console.log('getting latest json test data from ddragon...\n');

  console.log('getting versions json test data...');

  getResponse('http://ddragon.leagueoflegends.com/api/versions.json', (rawVersionsData) => {
    const versions = JSON.parse(rawVersionsData);

    const latestVersion = versions[0];
    console.log(`found the latest version to be '${latestVersion}'`);

    checkIsNewVersion(latestVersion);

    writeFile('versions_data_dragon', rawVersionsData);

    // Get rest of the test data
    getChampions(latestVersion);
    getItems(latestVersion);
    getMaps(latestVersion);
    getSummonerSpells(latestVersion);
  });
})();
