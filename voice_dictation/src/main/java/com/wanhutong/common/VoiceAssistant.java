package com.wanhutong.common;

import com.iflytek.cloud.speech.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.dictionary.DictionaryFactory;
import org.apdplat.word.segmentation.SegmentationAlgorithm;
import org.apdplat.word.segmentation.Word;
import org.apdplat.word.util.WordConfTools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

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

    private static final String APP_ID = "5b3c2e82";
    private static VoiceAssistant voiceAssistant;
    private static StringBuffer mResult = new StringBuffer();
    @Getter
    private String sRecognizeResults;
    /**
     * 听写
     */
    @Getter
    private boolean mIsEndOfSpeech = false;

    public static void main(String[] args) {
        File file = new File("C:\\Users\\Dream\\Desktop\\test_voice\\yibaoluo(1).amr");
        AmrToWav.changeToWav(file, "C:\\Users\\Dream\\Desktop\\test_voice\\hh.wav", 60f);
        VoiceAssistant instance = getInstance();
        instance.recognize(new File("C:\\Users\\Dream\\Desktop\\test_voice\\1234567.wav"), 16000);
        while (!instance.mIsEndOfSpeech) {
            log.debug("waitting....");
        }
        System.out.println(instance.sRecognizeResults);
//        WordConfTools.set("dic.path", "d:/dic.txt");
//        DictionaryFactory.reload();
//        List<Word> seg = WordSegmenter.seg(instance.sRecognizeResults, SegmentationAlgorithm.BidirectionalMaximumMatching);
//        for (Word word : seg) {
//            System.out.println(word.getAcronymPinYin() + "====" + word.getText());
//        }
    }

    public static VoiceAssistant getInstance() {
        if (voiceAssistant == null) {
            voiceAssistant = new VoiceAssistant(APP_ID);
        }
        return voiceAssistant;
    }

    public VoiceAssistant(String appId) {
        SpeechUtility.createUtility("appid=" + appId);
    }

    public void recognize(File recognitionFile, int sampleRate) {
        // 清空识别结果
        sRecognizeResults = "";
        if (SpeechRecognizer.getRecognizer() == null) {
            SpeechRecognizer.createRecognizer();
        }
        mIsEndOfSpeech = false;
        recognizePcmFileByte(recognitionFile, sampleRate);
    }

    /**
     * 自动化测试注意要点 如果直接从音频文件识别，需要模拟真实的音速，防止音频队列的堵塞
     */
    private void recognizePcmFileByte(File recognitionFile, int sampleRate) {
        SpeechRecognizer recognizer = SpeechRecognizer.getRecognizer();
        recognizer.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");
        recognizer.setParameter(SpeechConstant.AUDIO_FORMAT, "pcm");
        recognizer.setParameter(SpeechConstant.ASR_PTT, "0");
        //写音频流时，文件是应用层已有的，不必再保存
//		recognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH,
//				"./iat_test.pcm");
        recognizer.setParameter(SpeechConstant.RESULT_TYPE, "plain");
        recognizer.setParameter(SpeechConstant.SAMPLE_RATE, String.valueOf(sampleRate));
        recognizer.startListening(recListener);

        FileInputStream fis = null;
        final byte[] buffer = new byte[64 * 1024];
        try {
            fis = new FileInputStream(recognitionFile);
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
        }

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
            log.info("onResult enter");
            mResult.append(result.getResultString());
            if (islast) {
                log.info("识别结果为:" + mResult.toString());
                sRecognizeResults = mResult.toString();
                mIsEndOfSpeech = true;
                mResult.delete(0, mResult.length());
                waitupLoop();
            }
        }

        @Override
        public void onError(SpeechError error) {
            mIsEndOfSpeech = true;
            log.info("*************" + error.getErrorCode()
                    + "*************");
            waitupLoop();
        }

        @Override
        public void onEvent(int eventType, int arg1, int agr2, String msg) {
            log.info("onEvent enter");
        }

    };

    private void waitupLoop() {
        synchronized (this) {
            VoiceAssistant.this.notify();
        }
    }


}
