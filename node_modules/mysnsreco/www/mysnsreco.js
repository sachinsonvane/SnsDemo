
exports.callSpeak = function (text, onfulfilled, onrejected) {
    var options = {};

    if (typeof text == 'string') {
        options.text = text;
    } else {
        options = text;
    }

    cordova
        .exec(function () {
            onfulfilled();
        }, function (reason) {
            onrejected(reason);
        }, 'mysnsreco', 'callSpeak', [options]);
};

exports.callSpeakToText = function (text, onfulfilled, onrejected) {
    var options = {};

    if (typeof text == 'string') {
        options.text = text;
    } else {
        options = text;
    }

    cordova.exec(function (successRes) {
            onfulfilled(successRes);
        }, function (reason) {
            onrejected(reason);
        }, 'mysnsreco', 'callSpeakToText', [options]);
};