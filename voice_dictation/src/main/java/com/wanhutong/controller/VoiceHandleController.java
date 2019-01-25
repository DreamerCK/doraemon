package com.wanhutong.controller;

import com.wanhutong.MediaFile;
import com.wanhutong.common.*;
import com.wanhutong.common.error.BusinessErrorEnum;
import com.wanhutong.config.WordSegmentationConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.wanhutong.MediaFile.FILE_TYPE_WAV;

/**
 * (C) Copyright 2017-now
 * All rights reserved.
 * <p>
 * 语音文件处理控制层
 *
 * @author DreamerCK
 * @date 2019-01-16 16:26
 **/
@Slf4j
@RestController
@RequestMapping("/api/voice")
public class VoiceHandleController {

    @Resource
    private WordSegmentationConfig wordConfig;

    @PostMapping(value = "/file/recognition", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultVo recognizeVoiceFileToWord(@RequestParam(value = "voiceFile") MultipartFile file) throws IOException {
        File targetFile = null, convertFile = null, middleFile = null;
        try {
            //校验是否音频文件
            String originalFilename = file.getOriginalFilename();
            if (StringUtils.isBlank(originalFilename)) {
                log.warn("=====originalFilename is null");
                return ResultVo.createByErrorEnum(BusinessErrorEnum.VOICE_CAN_NOT_UNDERSTAND);
            }
            convertFile = new File(FileHandleUtils.createTempFilePath(wordConfig.getVoiceTemp(), originalFilename));
            MediaFile.MediaFileType fileType = MediaFile.getFileType(convertFile.getAbsolutePath());
            if (Objects.isNull(fileType) || !MediaFile.isAudioFileType(fileType.getFileType())) {
                log.warn("=====convertFile:{}=========fileType:{}", convertFile, fileType);
                return ResultVo.createByErrorEnum(BusinessErrorEnum.VOICE_CAN_NOT_UNDERSTAND);
            }
            //如果音频文件为.wav 格式的需要先转为其他音频格式如.mp3,再通过其他格式转为.wav格式
            log.info("=====convertFile:{}", convertFile.getAbsolutePath());
            if (FILE_TYPE_WAV != fileType.getFileType()) {
                FileUtils.copyInputStreamToFile(file.getInputStream(), convertFile);
                String targetFilePath = FileHandleUtils.createTempFilePath(wordConfig.getVoiceTemp(), FileHandleUtils.builder(FileHandleUtils.getFileNameWithoutSuffix(originalFilename)).append(wordConfig.getWavSuffixName()).toString());
                log.info("=====targetFilePath:{}", targetFilePath);
                targetFile = AudioConverter.changeToWav(convertFile, targetFilePath, 60f);
                log.info("=====targetFile1:{}", targetFile);
            } else {
                FileUtils.copyInputStreamToFile(file.getInputStream(), convertFile);
                String middleFilePath = FileHandleUtils.createTempFilePath(wordConfig.getVoiceTemp(), FileHandleUtils.builder(FileHandleUtils.getFileNameWithoutSuffix(originalFilename)).append(wordConfig.getMp3SuffixName()).toString());
                log.info("=====middleFilePath:{}", middleFilePath);
                middleFile = AudioConverter.chanageToMp3(convertFile, middleFilePath);
                String targetFilePath = FileHandleUtils.createTempFilePath(wordConfig.getVoiceTemp(), FileHandleUtils.builder(FileHandleUtils.getFileNameWithoutSuffix(originalFilename)).append(wordConfig.getWavSuffixName()).toString());
                targetFile = AudioConverter.changeToWav(middleFile, targetFilePath, 60f);
                log.info("=====targetFile2:{}", targetFile);
            }
            //语音识别
            VoiceAssistant instance = VoiceAssistant.getInstance();
            log.info(targetFile.exists() + "");
            instance.recognize(targetFile, 16000);
            log.info(targetFile.exists() + "");
            while (!instance.isMIsEndOfSpeech()) {
                log.debug("waiting....");
            }
            String recognizeResult = instance.getSRecognizeResults();
            if (StringUtils.isBlank(recognizeResult)) {
                log.warn("=====recognizeResult:{}", recognizeResult);
                return ResultVo.createByErrorEnum(BusinessErrorEnum.VOICE_CAN_NOT_UNDERSTAND);
            }
            //识别文字进行关键字切割
            List<String> segWord = WordSegmenter.seg(recognizeResult).parallelStream().map(Word::getText).collect(Collectors.toList());
            return ResultVo.createBySuccessData(VoiceResultVo.builder().resultStr(recognizeResult).segWords(segWord).build());
        } finally {
            if (!Objects.isNull(targetFile) && targetFile.exists()) {
                log.info(targetFile.getAbsolutePath());
                //noinspection ResultOfMethodCallIgnored
                targetFile.delete();
            }
            if (!Objects.isNull(middleFile) && middleFile.exists()) {
                log.info(middleFile.getAbsolutePath());
                //noinspection ResultOfMethodCallIgnored
                middleFile.delete();
            }
            if (!Objects.isNull(convertFile) && convertFile.exists()) {
                log.info(convertFile.getAbsolutePath());
                //noinspection ResultOfMethodCallIgnored
                convertFile.delete();
            }
        }
    }

}
