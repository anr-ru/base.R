/**
 * 
 */
package ru.anr.math.r;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.rosuda.JRI.REXP;

import ru.anr.base.BaseParent;

/**
 * A simple wrapper around {@link REXP} values.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 30, 2017
 *
 */

public class RResult extends BaseParent {

    /**
     * Some resulted R value
     */
    private final REXP r;

    /**
     * The name of this result variable
     */
    private final String name;

    /**
     * The main constructor
     * 
     * @param name
     *            The name of a variable
     * @param r
     *            The value of the variable
     */
    public RResult(String name, REXP r) {

        super();
        this.r = r;
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {

        return name;
    }

    /**
     * @return the result value as a double
     */
    public BigDecimal asDecimal() {

        return scale(new BigDecimal(r.asDouble()), 8);
    }

    /**
     * @return the result value as a string
     */
    public String asString() {

        return r.asString();
    }

    /**
     * @return the result as a big decimal array
     */
    public List<BigDecimal> asDecimals() {

        double[] array = r.asDoubleArray();
        return list(list(ArrayUtils.toObject(array)).stream().map(v -> scale(new BigDecimal(v), 8)));
    }

    /**
     * @return the result as an integer array
     */
    public List<String> asStrings() {

        return list(r.asStringArray());
    }
}
