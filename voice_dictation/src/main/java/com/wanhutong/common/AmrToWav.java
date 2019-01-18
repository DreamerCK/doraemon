package com.wanhutong.common;

import ws.schild.jave.*;

import java.io.File;

/**
 * (C) Copyright 2017-now
 * All rights reserved.
 *
 * @author DreamerCK
 * @date 2018-11-29 17:33
 **/
public class AmrToWav {

    public static void changeToWav(File source, String targetPath, Float duration) {
        try {
            File target = new File(targetPath);
            CustomFFMPEGLocator customFFMPEGLocator = new CustomFFMPEGLocator();
            Encoder encoder = new Encoder(customFFMPEGLocator);
            AudioAttributes audio = new AudioAttributes();
            //设置解码格式，比特率，位数，声道等信息
            audio.setCodec("pcm_s16le");
            audio.setBitRate(705000);
            audio.setChannels(1);
            audio.setSamplingRate(16000);
            audio.setVolume(10000);
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setFormat("wav");
            attrs.setAudioAttributes(audio);
            attrs.setDuration(duration);
            MultimediaObject multimediaSource = new MultimediaObject(source, customFFMPEGLocator);
            encoder.encode(multimediaSource, target, attrs);
        } catch (IllegalArgumentException | EncoderException e) {
            e.printStackTrace();
        }
    }

}
