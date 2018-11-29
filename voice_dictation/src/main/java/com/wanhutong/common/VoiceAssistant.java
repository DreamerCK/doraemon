package com.wanhutong.common;

import com.iflytek.cloud.speech.*;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * (C) Copyright 2017-now
 * All rights reserved.
 * <p>
 * 语音助手
 *
 * @author DreamerCK
 * @date 2018-11-29 14:39
 **/
@Slf4j
public class VoiceAssistant {

    private static final String APPID = "5b3c2e82";

    private static StringBuffer mResult = new StringBuffer();

    public static void main(String[] args) {
        SpeechUtility.createUtility("appid=" + APPID);
        VoiceAssistant voiceAssistant = new VoiceAssistant();
        voiceAssistant.recognize();
    }


    // *************************************音频流听写*************************************

    /**
     * 听写
     */
    private boolean mIsEndOfSpeech = false;

    private void recognize() {
        if (SpeechRecognizer.getRecognizer() == null) {
            SpeechRecognizer.createRecognizer();
        }
        mIsEndOfSpeech = false;
        recognizePcmFileByte();
    }

    /**
     * 自动化测试注意要点 如果直接从音频文件识别，需要模拟真实的音速，防止音频队列的堵塞
     */
    private void recognizePcmFileByte() {
        SpeechRecognizer recognizer = SpeechRecognizer.getRecognizer();
        recognizer.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");
        //写音频流时，文件是应用层已有的，不必再保存
//		recognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH,
//				"./iat_test.pcm");
        recognizer.setParameter(SpeechConstant.RESULT_TYPE, "plain");
        recognizer.startListening(recListener);

        FileInputStream fis = null;
        final byte[] buffer = new byte[64 * 1024];
        try {
            fis = new FileInputStream(new File("C://Users//Lenovo/Desktop//test_voice//test.pcm"));
            if (0 == fis.available()) {
                mResult.append("no audio avaible!");
                recognizer.cancel();
            } else {
                int lenRead = buffer.length;
                while (buffer.length == lenRead && !mIsEndOfSpeech) {
                    lenRead = fis.read(buffer);
                    recognizer.writeAudio(buffer, 0, lenRead);
                }//end of while

                recognizer.stopListening();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fis) {
                    fis.close();
                    fis = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }//end of try-catch-finally

    }

    /**
     * 听写监听器
     */
    private RecognizerListener recListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            log.info("onBeginOfSpeech enter");
            log.info("*************开始录音*************");
        }

        @Override
        public void onEndOfSpeech() {
            log.info("onEndOfSpeech enter");
            mIsEndOfSpeech = true;
        }

        @Override
        public void onVolumeChanged(int volume) {
            log.info("onVolumeChanged enter");
            if (volume > 0) {
                log.info("*************音量值:" + volume + "*************");
            }
        }

        @Override
        public void onResult(RecognizerResult result, boolean islast) {
            log.info("onResult enter");
            mResult.append(result.getResultString());

            if (islast) {
                log.info("识别结果为:" + mResult.toString());
                mIsEndOfSpeech = true;
                mResult.delete(0, mResult.length());
            }
        }

        @Override
        public void onError(SpeechError error) {
            mIsEndOfSpeech = true;
            log.info("*************" + error.getErrorCode()
                    + "*************");
        }

        @Override
        public void onEvent(int eventType, int arg1, int agr2, String msg) {
            log.info("onEvent enter");
        }

    };


}
