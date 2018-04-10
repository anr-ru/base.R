/**
 * 
 */
package ru.anr.math.r;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.rosuda.JRI.Rengine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import ru.anr.base.ApplicationException;
import ru.anr.base.services.BaseServiceImpl;

/**
 * An implementation of {@link RService}.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 30, 2017
 *
 */

public class RServiceImpl extends BaseServiceImpl implements RService {

    /**
     * The logger
     */
    private static final Logger logger = LoggerFactory.getLogger(RServiceImpl.class);

    /**
     * The main rJava library
     */
    private Rengine engine;

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {

        super.init();

        // R initialization
        Rengine.versionCheck();
        engine = new Rengine(new String[]{ "--no-save" }, false, new DefaultConsole());

        engine.waitForR();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S> Map<String, RResult> eval(Map<String, S> variables, List<String> scripts, String... outputNames) {

        Assert.notNull(engine, "R Engine not initialized");

        variables.forEach((k, v) -> assignVariable(k, v));

        String script = list(scripts).stream().map(f -> readAsString(f)).collect(Collectors.joining("\n"));

        /*
         * Screen the " symbols
         */
        script = script.replaceAll("\"", "'");

        if (logger.isDebugEnabled()) {
            logger.debug("The script to invoke:\n{}", script);
        }

        engine.eval("eval(parse(text=\"" + script + "\"))");

        return list(outputNames).stream().map(n -> new RResult(n, engine.eval(n)))
                .collect(Collectors.toMap(RResult::getName, r -> r));
    }

    /**
     * Assigns the given value to the variable
     * 
     * @param name
     *            The name of the variable
     * @param value
     *            The value of the variable
     */
    private void assignVariable(String name, Object value) {

        if (value instanceof BigDecimal) {

            BigDecimal v = (BigDecimal) value;
            engine.assign(name, new double[]{ v.doubleValue() });

        } else if (value instanceof BigDecimal[]) {

            BigDecimal[] v = (BigDecimal[]) value;
            engine.assign(name, toArray(list(v).stream()));

        } else if (value instanceof List<?>) {

            @SuppressWarnings("unchecked")
            List<BigDecimal> v = (List<BigDecimal>) value;
            engine.assign(name, toArray(v.stream()));
        } else {
            throw new ApplicationException("Unable to set the variable " + name + " of type "
                    + ((value == null) ? "null" : value.getClass()));
        }
    }

    /**
     * Converts a stream of {@link BigDecimal} values to an array of doubles
     * 
     * @param stream
     *            A stream
     * @return The resulted array
     */
    private double[] toArray(Stream<BigDecimal> stream) {

        return ArrayUtils.toPrimitive(stream.map(i -> i.doubleValue()).toArray(Double[]::new));
    }
}
