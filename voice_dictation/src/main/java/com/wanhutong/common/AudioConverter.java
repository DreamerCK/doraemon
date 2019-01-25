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
public class AudioConverter {

    public static File changeToWav(File source, String targetPath, Float duration) {
        try {
            File target = new File(targetPath);
            Encoder encoder = new Encoder();
            AudioAttributes audio = new AudioAttributes();
            //设置解码格式，比特率，位数，声道等信息
            audio.setCodec("pcm_s16le");
            audio.setBitRate(705000);
            audio.setChannels(1);
            audio.setSamplingRate(16000);
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setFormat("wav");
            attrs.setAudioAttributes(audio);
            attrs.setDuration(duration);
            MultimediaObject multimediaObject = new MultimediaObject(source);
            encoder.encode(multimediaObject, target, attrs);
            return target;
        } catch (IllegalArgumentException | EncoderException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static File chanageToMp3(File source, String targetPath) {
        try {
            File target = new File(targetPath);
            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("libmp3lame");
            audio.setBitRate(128000);
            audio.setChannels(2);
            audio.setSamplingRate(44100);
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setFormat("mp3");
            attrs.setAudioAttributes(audio);
            Encoder encoder = new Encoder();
            MultimediaObject multimediaObject = new MultimediaObject(source);
            encoder.encode(multimediaObject, target, attrs);
            return target;
        } catch (IllegalArgumentException | EncoderException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        File file = chanageToMp3(new File("C:\\Users\\Lenovo\\Desktop\\fsdownload\\Recorder_049.wav"), "C:\\Users\\Lenovo\\AppData\\Local\\Temp\\voice_temp\\Recorder_049.mp3");
        System.out.println(file.getAbsolutePath());
    }

}
