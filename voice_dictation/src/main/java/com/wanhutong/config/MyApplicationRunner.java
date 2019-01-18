package com.wanhutong.config;

import org.apdplat.word.WordSegmenter;
import org.apdplat.word.dictionary.DictionaryFactory;
import org.apdplat.word.util.WordConfTools;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * (C) Copyright 2017-now
 * All rights reserved.
 * <p>
 * 项目启动的时候立刻执行指定方法
 *
 * @author DreamerCK
 * @date 2019-01-16 17:09
 **/
@Order(1)
@Component
public class MyApplicationRunner implements ApplicationRunner {

    private static final String DIC_PATH = "dic.path";
    private static final String STOP_WORDS_PATH = "stopwords.path";
    private static final String BIG_RAM_PATH = "bigram.path";

    @Resource
    private WordSegmentationConfig config;

    @Override
    public void run(ApplicationArguments args){
        WordConfTools.set(DIC_PATH, config.getDicPath());
        WordConfTools.set(STOP_WORDS_PATH, config.getStopWordsPath());
        WordConfTools.set(BIG_RAM_PATH, config.getBigRamPath());
        DictionaryFactory.reload();
        WordSegmenter.seg(config.getTestSegWord());
    }
}
