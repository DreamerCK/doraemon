package com.wanhutong.common;

import lombok.extern.slf4j.Slf4j;
import ws.schild.jave.FFMPEGLocator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * (C) Copyright 2017-now
 * All rights reserved.
 * <p>
 * 自定义ffmpeg定位器
 *
 * @author DreamerCK
 * @date 2019-01-16 10:12
 **/
@Slf4j
public class CustomFFMPEGLocator extends FFMPEGLocator {

    private static final String MY_EXE_VERSION = "2.4.4";

    private final String path;

    public CustomFFMPEGLocator() {

        String os = System.getProperty("os.name").toLowerCase();
        boolean isWindows = os.contains("windows");
        boolean isMac = os.contains("mac");

        // Dir Folder
        File dirFolder = new File(System.getProperty("user.dir"), "jave/");
        if (!dirFolder.exists())
        {
            dirFolder.mkdirs();
        }

        // -----------------ffmpeg executable export on disk.-----------------------------
        String suffix = isWindows ? ".exe" : (isMac ? "-osx" : "");
        String arch = System.getProperty("os.arch");

        //File
        File ffmpegFile = new File(dirFolder, "ffmpeg-" + arch +"-"+MY_EXE_VERSION+ suffix);
        log.debug("Executable path: "+ffmpegFile.getAbsolutePath());

        //Check the version of existing .exe file
        if (ffmpegFile.exists())
        {
            // OK, already present
        }
        else
        {
            copyFile("ffmpeg-" + arch + suffix, ffmpegFile);
        }

        // Need a chmod?
        if (!isWindows)
        {
            try
            {
                Runtime.getRuntime().exec(new String[]
                        {
                                "/bin/chmod", "755", ffmpegFile.getAbsolutePath()
                        });
            } catch (IOException e)
            {
                log.error(String.valueOf(e));
            }
        }

        // Everything seems okay
        path = ffmpegFile.getAbsolutePath();
        log.debug("ffmpeg executable found: "+path);
    }

    @Override
    protected String getFFMPEGExecutablePath() {
        return path;
    }

    private void copyFile(String path, File dest) {
        String resourceName= "native/" + path;
        try
        {
            copy(getClass().getResourceAsStream(resourceName), dest.getAbsolutePath());
        }
        catch (NullPointerException ex)
        {
            log.error("Could not find ffmpeg executable for "+resourceName+" is the correct platform jar included?");
            throw ex;
        }
    }

    private boolean copy(InputStream source, String destination) {
        boolean success = true;

        try
        {
            Files.copy(source, Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex)
        {
            log.warn("Cannot write file " + destination, ex);
            success = false;
        }

        return success;
    }
}
