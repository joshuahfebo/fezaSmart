const { getDefaultConfig } = require('expo/metro-config');

const config = getDefaultConfig(__dirname);

// Suppress React Native package.json "exports" resolution warnings
// by disabling the conditionNames resolver for problematic packages
config.resolver.unstable_enablePackageExports = false;

module.exports = config;
