/**
 * 
 */
package ru.anr.math.r;

import java.math.BigDecimal;
import java.util.Vector;

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

        return new BigDecimal(r.asDouble());
    }

    /**
     * @return the result as a big decimal array
     */
    public BigDecimal[] asDecimals() {

        double[] array = r.asDoubleArray();
        return list(ArrayUtils.toObject(array)).stream().map(v -> new BigDecimal(v)).toArray(BigDecimal[]::new);
    }

    /**
     * @return the result as an integer array
     */
    public Integer[] asIntegers() {

        int[] array = r.asIntArray();
        return list(ArrayUtils.toObject(array)).stream().map(v -> Integer.valueOf(v)).toArray(Integer[]::new);
    }

    /**
     * @return the result as an integer array
     */
    public String[] asStrings() {

        return r.asStringArray();
    }

    /**
     * @return the result as a big decimal array
     */
    @SuppressWarnings("unchecked")
    public <S> Vector<S> asVector() {

        return r.asVector();
    }
}
