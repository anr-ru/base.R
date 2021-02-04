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
     * The main rJava library: it's loaded one time per JVM
     */
    private static Rengine engine;

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void init() {

        super.init();

        if (engine == null) {
            logger.info("Starting R Engine ...");
            // R initialization
            Rengine.versionCheck();
            engine = new Rengine(new String[]{"--no-save", "--quiet"}, false, new DefaultConsole());

            engine.waitForR();
        }
        else {
            logger.info("R engine already loaded");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S> Map<String, RResult> eval(Map<String, S> variables, List<String> scripts, String... outputNames) {

        Assert.notNull(engine, "R Engine not initialized");

        variables.forEach(this::assignVariable);

        String script = list(scripts).stream().map(this::getScript).collect(Collectors.joining("\n"));

        /*
         * Screen the " symbols
         */
        script = script.replaceAll("\"", "'");

        if (logger.isDebugEnabled()) {
            logger.debug("Inputs:");
            variables.forEach((k, v) -> {
                logger.debug("{}={}", k, v);
            });
        }

        if (logger.isTraceEnabled()) {
            logger.trace("The script to invoke:\n{}", script);
        }

        engine.eval("eval(parse(text=\"" + script + "\"))");

        Map<String, RResult> rs = list(outputNames).stream()
                .map(n -> new RResult(n, engine.eval(n)))
                .collect(Collectors.toMap(RResult::getName, r -> r));

        if (logger.isDebugEnabled()) {
            logger.debug("Outputs:");
            rs.forEach((k, v) -> logger.debug("{}={}", k, v));
        }
        return rs;
    }

    /**
     * Loads a script from the given file or use the path variable as a script
     * itself.
     * 
     * @param path
     *            Possible a path to an R-file or just a script.
     * @return Script data
     */
    private String getScript(String path) {

        return path.endsWith(".R") ? readAsString(path) : path;
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

        } else if (value instanceof String) {

            engine.assign(name, (String) value);

        } else if (value instanceof Boolean) {

            engine.assign(name, new boolean[]{ (Boolean) value });

        } else if (value instanceof List<?>) {

            Class<?> clazz = getItemClass((List<?>) value);
            if (clazz == null) {
                engine.assign(name, new String[]{});
            } else {
                if (clazz.isAssignableFrom(String.class)) {
                    @SuppressWarnings("unchecked")
                    List<String> v = (List<String>) value;
                    engine.assign(name, toString(v.stream()));

                } else if (clazz.isAssignableFrom(BigDecimal.class)) {
                    @SuppressWarnings("unchecked")
                    List<BigDecimal> v = (List<BigDecimal>) value;
                    engine.assign(name, toDouble(v.stream()));
                } else {
                    throw new ApplicationException("Unsupported type of list items: " + value.getClass());
                }
            }
        } else {
            throw new ApplicationException("Unable to set the variable " + name + " of type "
                    + ((value == null) ? "null" : value.getClass()));
        }
    }

    /**
     * Determines the class of the list's item
     * 
     * @param values
     *            A list variable
     * @return The resulted found class
     */
    public static Class<?> getItemClass(List<?> values) {

        return values.size() == 0 ? null : values.get(0).getClass();
    }

    /**
     * Converts a stream of {@link BigDecimal} values to an array of doubles
     * 
     * @param stream
     *            A stream
     * @return The resulted array
     */
    private double[] toDouble(Stream<BigDecimal> stream) {
        return ArrayUtils.toPrimitive(stream.map(BigDecimal::doubleValue).toArray(Double[]::new));
    }

    /**
     * Converts a stream of {@link String} to an array of strings
     * 
     * @param stream
     *            A stream
     * @return The resulted arrays
     */
    private String[] toString(Stream<String> stream) {
        return stream.toArray(String[]::new);
    }
}
