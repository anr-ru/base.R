/*
 * Copyright 2014-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package ru.anr.math.r;

import org.rosuda.JRI.RMainLoopCallbacks;
import org.rosuda.JRI.Rengine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * An implementation of console log callbacks.
 *
 * @author Alexey Romanchuk
 * @created Nov 30, 2017
 */
public class DefaultConsole implements RMainLoopCallbacks {

    private static final Logger logger = LoggerFactory.getLogger(DefaultConsole.class);

    @Override
    public void rWriteConsole(Rengine re, String text, int oType) {
        logger.debug("{}", text);
    }

    @Override
    public void rBusy(Rengine re, int which) {
        logger.warn("rBusy({})", which);
    }

    @Override
    public String rReadConsole(Rengine re, String prompt, int addToHistory) {

        logger.info(prompt);
        String s = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            s = br.readLine();
        } catch (Exception e) {
            logger.error("jriReadConsole exception: {}", e.getMessage());
        }
        return (s == null || s.length() == 0) ? s : s + "\n";
    }

    @Override
    public void rShowMessage(Rengine re, String message) {
        logger.info("rShowMessage \"{}\"", message);
    }

    @Override
    public String rChooseFile(Rengine re, int newFile) {
        return null;
    }

    @Override
    public void rFlushConsole(Rengine re) {
        // Empty
    }

    @Override
    public void rLoadHistory(Rengine re, String filename) {

        // Empty
    }

    @Override
    public void rSaveHistory(Rengine re, String filename) {

        // Empty
    }
}
