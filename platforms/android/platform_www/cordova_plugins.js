cordova.define('cordova/plugin_list', function(require, exports, module) {
  module.exports = [
    {
      "id": "cordova-mysnsreco.mysnsreco",
      "file": "plugins/cordova-mysnsreco/www/mysnsreco.js",
      "pluginId": "cordova-mysnsreco",
      "clobbers": [
        "SNS"
      ]
    }
  ];
  module.exports.metadata = {
    "cordova-plugin-whitelist": "1.3.4",
    "cordova-mysnsreco": "0.0.1"
  };
});