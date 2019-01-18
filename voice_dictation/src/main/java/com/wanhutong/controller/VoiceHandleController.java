package com.wanhutong.controller;

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
import java.util.stream.Collectors;

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

    @PostMapping(value = "/file/recognition", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResultVo recognizeVoiceFileToWord(@RequestParam(value = "voiceFile") MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename)) {
            return ResultVo.createByErrorEnum(BusinessErrorEnum.VOICE_FILE_CAN_NOT_RECOGNIZE);
        }
        File convertFile = new File(FileHandleUtils.createTempFilePath(wordConfig.getVoiceTemp(), originalFilename));
        FileUtils.copyInputStreamToFile(file.getInputStream(), convertFile);
        String targetFilePath = FileHandleUtils.createTempFilePath(wordConfig.getVoiceTemp(), FileHandleUtils.builder(FileHandleUtils.getFileNameWithoutSuffix(originalFilename)).append(wordConfig.getSuffixName()).toString());
        System.out.println(targetFilePath);
        AmrToWav.changeToWav(convertFile, targetFilePath, 60f);
        VoiceAssistant instance = VoiceAssistant.getInstance();
        instance.recognize(targetFilePath, 16000);
        while (!instance.isMIsEndOfSpeech()) {
            log.debug("waiting....");
        }
        String recognizeResult = instance.getSRecognizeResults();
        List<String> segWord = WordSegmenter.seg(recognizeResult).parallelStream().map(Word::getText).collect(Collectors.toList());
        return ResultVo.createBySuccessData(VoiceResultVo.builder().resultStr(recognizeResult).segWords(segWord).build());
    }

}