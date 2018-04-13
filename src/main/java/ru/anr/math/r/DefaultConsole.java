/**
 * 
 */
package ru.anr.math.r;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.rosuda.JRI.RMainLoopCallbacks;
import org.rosuda.JRI.Rengine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of callbacks.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 30, 2017
 *
 */
public class DefaultConsole implements RMainLoopCallbacks {

    /**
     * The logger
     */
    private static final Logger logger = LoggerFactory.getLogger(DefaultConsole.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public void rWriteConsole(Rengine re, String text, int oType) {

        logger.debug("{}", text);
    }

    /**
     * {@inheritDoc}
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public void rBusy(Rengine re, int which) {

        logger.warn("rBusy({})", which);
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void rShowMessage(Rengine re, String message) {

        logger.info("rShowMessage \"{}\"", message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String rChooseFile(Rengine re, int newFile) {

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rFlushConsole(Rengine re) {

        // Empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rLoadHistory(Rengine re, String filename) {

        // Empty
    }

    /**
     * {@inheritDoc}
     */

    @Override
    public void rSaveHistory(Rengine re, String filename) {

        // Empty
    }
}
