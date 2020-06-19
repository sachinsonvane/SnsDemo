package com.cordova.mysnsreco;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.UtteranceProgressListener;
import java.util.HashMap;
import java.util.Locale;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.os.Bundle;
import android.view.View;
import java.util.ArrayList;
import android.app.Activity;
import android.app.Activity.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;


public class SNS extends CordovaPlugin implements OnInitListener {

    public static final String ERROR_INVALID_OPTIONS = "ERROR_INVALID_OPTIONS";
    public static final String ERROR_NOT_INITIALIZED = "ERROR_NOT_INITIALIZED";
    public static final String ERROR_INITIALIZING = "ERROR_INITIALIZING";
    public static final String ERROR_UNKNOWN = "ERROR_UNKNOWN";
    public static final String ERROR_MGS1 = "Sorry your device not supported";
    
    boolean mTxtSpInitialized = false;
    TextToSpeech mTxtSp = null;
    private final int REQ_CODE = 100;
    public CallbackContext mCallbackContext = null;
    private Activity activity;
    private Context context;
    public static SNS mSNS = null;

    @Override
    public void initialize(CordovaInterface cordova, final CordovaWebView webView) {
        mSNS = this;
        activity = cordova.getActivity();
        context = webView.getContext();
        mTxtSp = new TextToSpeech(cordova.getActivity().getApplicationContext(), this);
        mTxtSp.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {
                // do nothing
            }

            @Override
            public void onDone(String callbackId) {
                if (!callbackId.equals("")) {
                    CallbackContext context = new CallbackContext(callbackId, webView);
                    context.success();
                }
            }

            @Override
            public void onError(String callbackId) {
                if (!callbackId.equals("")) {
                    CallbackContext context = new CallbackContext(callbackId, webView);
                    context.error(ERROR_UNKNOWN);
                }
            }
        });
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext)
            throws JSONException {
        if (action.equals("callSpeak")) {
            callSpeak(args, callbackContext);
        }else if (action.equals("callSpeakToText")) {
            callSpeakToText(args, callbackContext);
        }else {
            return false;
        }
        return true;
    }

    @Override
    public void onInit(int status) {
        if (status != TextToSpeech.SUCCESS) {
            mTxtSp = null;
        } else {
            // warm up the tts engine with an empty string
            HashMap<String, String> txtSpParams = new HashMap<String, String>();
            txtSpParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "");
            mTxtSp.setLanguage(new Locale("en", "US"));
            mTxtSp.speak("", TextToSpeech.QUEUE_FLUSH, txtSpParams);

            mTxtSpInitialized = true;
        }
    }

    private void callSpeak(JSONArray args, CallbackContext callbackContext)
            throws JSONException, NullPointerException {
        JSONObject params = args.getJSONObject(0);

        if (params == null) {
            callbackContext.error(ERROR_INVALID_OPTIONS);
            return;
        }

        String text;
        String locale;
        double rate;

        if (params.isNull("text")) {
            callbackContext.error(ERROR_INVALID_OPTIONS);
            return;
        } else {
            text = params.getString("text");
        }

        if (params.isNull("locale")) {
            locale = "en-US";
        } else {
            locale = params.getString("locale");
        }

        if (params.isNull("rate")) {
            rate = 1.0;
        } else {
            rate = params.getDouble("rate");
        }

        if (mTxtSp == null) {
            callbackContext.error(ERROR_INITIALIZING);
            return;
        }

        if (!mTxtSpInitialized) {
            callbackContext.error(ERROR_NOT_INITIALIZED);
            return;
        }

        HashMap<String, String> txtSpParams = new HashMap<String, String>();
        txtSpParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, callbackContext.getCallbackId());

        String[] localeArgs = locale.split("-");
        mTxtSp.setLanguage(new Locale(localeArgs[0], localeArgs[1]));
        mTxtSp.setSpeechRate((float) rate);

        mTxtSp.speak(text, TextToSpeech.QUEUE_FLUSH, txtSpParams);
    }

    private void callSpeakToText(JSONArray args, CallbackContext callbackContext)
            throws JSONException, NullPointerException {

        mCallbackContext =  callbackContext;      
        JSONObject params = args.getJSONObject(0);
        if (params == null) {
            callbackContext.error(ERROR_INVALID_OPTIONS);
            return;
        }
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Need to speak");
        try {
            activity.startActivityForResult(intent, REQ_CODE);
        } catch (ActivityNotFoundException a) {
            
            callbackContext.error(ERROR_MGS1);
            return;
        }
            
    }


}
