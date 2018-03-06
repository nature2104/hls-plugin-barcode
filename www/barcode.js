var exec = require('cordova/exec');

module.exports = {
    startScan: function (scannerSubtitle, successCallback, errorCallback) {
        exec(successCallback, errorCallback, "Barcode", "startScan", [scannerSubtitle]);
    },
    hasPermission: function (successCallback, errorCallback) {
      exec(successCallback, errorCallback, "Barcode", "hasPermission", []);
    }
};
