const execSync = require('child_process').execSync;
const fs = require('fs');
const os = require('os');
const readline = require('readline');

const packageJsonFile = './frontend/package.json';
const packageJsonLockFile = './frontend/package-lock.json';
const gradlePropertiesFile = './backend/gradle.properties';

const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout
});

/**
 * Gets the current version from the package.json file.
 *
 * @returns {string} the version in x.y.z or x.y.z-SNAPSHOT format
 */
function getCurrentVersion() {
  const packageJsonContent = fs.readFileSync(packageJsonFile, 'utf8');
  const packageJson = JSON.parse(packageJsonContent);
  return packageJson.version;
}

/**
 * Increments the revision of the given release version. Ex. 1.0.0 becomes 1.0.1
 *
 * @param {string} version the current version
 * @returns {string} the next version revision
 */
function incrementVersion(version) {
  const [major, minor, revision] = version.split('.');

  const newRevision = parseInt(revision, 10) + 1;

  return `${major}.${minor}.${newRevision}`;
}

/**
 * Updates the version property of package.json and package-lock.json files.
 *
 * @param {string} version the release version
 */
function updatePackageJson(version) {
  console.log('updating package.json...');
  const packageJsonContent = fs.readFileSync(packageJsonFile, 'utf8');
  const packageJson = JSON.parse(packageJsonContent);
  packageJson.version = version;
  fs.writeFileSync(packageJsonFile, JSON.stringify(packageJson, null, 2));

  console.log('updating package-lock.json...');
  const packageJsonLockContent = fs.readFileSync(packageJsonLockFile, 'utf8');
  const packageJsonLock = JSON.parse(packageJsonLockContent);
  packageJsonLock.version = version;
  fs.writeFileSync(packageJsonLockFile, JSON.stringify(packageJsonLock, null, 2));
}

/**
 * Updates the buildVersion property of the gradle.properties file.
 *
 * @param {string} version the release version
 */
function updateGradleProperties(version) {
  console.log('updating gradle.properties...');
  const gradlePropertiesContent = fs.readFileSync(gradlePropertiesFile, 'utf8');

  const gradleProperties = gradlePropertiesContent.split(os.EOL);
  let newGradleProperties = '';

  for (const property of gradleProperties) {
    if (property) {
      const [key, value] = property.split('=');
      if (key === 'buildVersion') {
        newGradleProperties += `${key}=${version}`;
      } else {
        newGradleProperties += `${key}=${value}`;
      }
      newGradleProperties += os.EOL;
    }
  }

  fs.writeFileSync(gradlePropertiesFile, newGradleProperties);
}

/**
 * Performs the following git operations:
 *
 * ```
 * git add package.json package-lock.json gradle.properties
 * git commit -m "message"
 * ```
 * @param {string} version the release version
 */
function gitCommit(version) {
  let commitMsg = '';
  if (isSnapshotVersion(version)) {
    commitMsg = 'Next snapshot version';
  } else {
    commitMsg = `Release ${version}`;
  }
  console.log(`committing changes with message: '${commitMsg}'`);

  execSync(`git add ${packageJsonFile} ${packageJsonLockFile} ${gradlePropertiesFile}`);
  execSync(`git commit -m "${commitMsg}"`);
}

/**
 * Updates the necessary files to include - package.json, package-lock.json and gradle.properties as part of the next
 * release process.
 *
 * @param {string} version the release version
 */
function updateFiles(version) {
  updatePackageJson(version);
  updateGradleProperties(version);
  gitCommit(version);
}

/**
 * Prompts for the next release version, which is optional.
 *
 * @param {string} defaultNextVersion the next version to be used if one is not specified
 */
function promptForNextVersion(defaultNextVersion) {
  rl.prompt();

  rl.on('line', (line) => {
    const nextVersion = line.trim();
    if (nextVersion) {
      console.log(`Next version will be: '${nextVersion}'`);
      updateFiles(nextVersion);
    } else {
      console.log(`Next version will be: '${defaultNextVersion}'`);
      updateFiles(defaultNextVersion);
    }
    process.exit(0);
  }).on('close', () => {
    process.exit(0);
  });
}

/**
 * Determines if the given version is a SNAPSHOT or full release version
 *
 * @param {string} buildVersion
 * @returns {boolean} true if SNAPSHOT version, otherwise false
 */
function isSnapshotVersion(buildVersion) {
  return buildVersion.includes('SNAPSHOT');
}

/**
 * Prepares for the next release either by incrementing the next version as x.y.z-SNAPSHOT or removing SNAPSHOT.
 */
function prepareNextRelease() {
  const currentVersion = getCurrentVersion();

  if (isSnapshotVersion(currentVersion)) {
    makeRelease(currentVersion);
  } else {
    nextSnapshot(currentVersion);
  }
}

/**
 * Prepares for the next release with the next SNAPSHOT version.
 *
 * @param {string} snapshotVersion the SNAPSHOT version
 */
function makeRelease(snapshotVersion) {
  const releaseVersion = snapshotVersion.substring(0, snapshotVersion.indexOf('-SNAPSHOT'));

  console.log('Preparing for release...');
  console.log(`Current snapshot: '${snapshotVersion}'`);
  console.log(`Releasing version: '${releaseVersion}'`);

  updateFiles(releaseVersion);
  process.exit(0);
}

/**
 * Prepares for the next release with the next SNAPSHOT version.
 *
 * @param {string} currentVersion the current release version
 */
function nextSnapshot(currentVersion) {
  const defaultNextVersion = incrementVersion(currentVersion);

  const snapshotVersion = `${defaultNextVersion}-SNAPSHOT`;

  console.log('Preparing for next version...');
  console.log(`Current version: '${currentVersion}'`);
  console.log(`Optionally enter next version. Will use: '${defaultNextVersion}' (${snapshotVersion}) if not specified`);

  promptForNextVersion(snapshotVersion);
}

prepareNextRelease();
