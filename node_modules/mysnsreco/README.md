
Install Plugin use following commond:
cordova plugin add C:\Users\UserName\Desktop\mysnsreco --save

If you want convert text to speech then use following code particular event.

SNS.callSpeak('hello, world!', function () {
    alert('success');
}, function (reason) {
    alert(reason);
});

If you want convert speech to text then use following code particular event.

SNS.callSpeakToText('start speech', function (successRes) {
        alert('success '+successRes);
}, function (reason) {
    alert(reason);
});


Need to add following code in MainActivity
C:\Users\UserName\Desktop\SnsDemo\platforms\android\app\src\main\java\com\snsdemo

import com.cordova.mysnsreco.SNS;
import android.speech.RecognizerIntent;
import java.util.ArrayList;
import android.content.Intent;

private final int REQ_CODE = 100;

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
        case REQ_CODE: {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                SNS.mSNS.mCallbackContext.success(result.get(0).toString());
            }
            break;
        }
    }
    super.onActivityResult(requestCode, resultCode, data);
}