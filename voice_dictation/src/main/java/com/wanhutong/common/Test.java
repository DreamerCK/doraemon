package com.wanhutong.common;

import ws.schild.jave.*;

import java.io.File;

/**
 * (C) Copyright 2017-now
 * All rights reserved.
 *
 * @author DreamerCK
 * @date 2018-11-29 11:30
 **/
public class Test {

    public static void main(String[] args) {
        try {
            String path = new StringBuilder().append("c:").append(File.separatorChar).append("Users").append(File.separatorChar).append("Lenovo")
                    .append(File.separatorChar).append("Desktop").append(File.separatorChar).append("6001.amr").toString();
            File source = new File("C://Users//Lenovo/Desktop//test_voice//iat_test_1.pcm");
//            if (!source.isFile()) {
//                System.out.println("false");
//                return;
//            }
//            MultimediaObject multimediaObject = new MultimediaObject(source);
//            File target = new File("target.wav");
//            AudioAttributes audioAttributes = new AudioAttributes();
//            audioAttributes.setCodec("pcm_s16le");
//            EncodingAttributes attrs = new EncodingAttributes();
//            attrs.setFormat("wav");
//            attrs.setAudioAttributes(audioAttributes);
//            Encoder encoder = new Encoder();
//            encoder.encode(multimediaObject, target, attrs);




            VoiceAssistant voiceAssistant = new VoiceAssistant();
//            voiceAssistant.recognize(source);
        } catch (Exception e) {
            e.printStackTrace();
        }





    }


}
