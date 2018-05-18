/**
 * 
 */
package ru.anr.math.r;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.rosuda.JRI.REXP;

import ru.anr.base.ApplicationException;
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
     * @return true, if it is a null value
     */
    public boolean isNull() {

        return this.r == null || this.r.rtype == REXP.XT_NULL;
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
    public double asDouble() {

        return r.asDouble();
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
        return list(list(ArrayUtils.toObject(array)).stream()
                .map(v -> Double.isNaN(v) || Double.isInfinite(v) ? null : scale(new BigDecimal(v), 8)));
    }

    /**
     * @return the result as an array of double values
     */
    public double[] asDoubles() {

        return r.asDoubleArray();
    }

    /**
     * @return returns the value as a double matrix
     */
    public double[][] asDecimalMatrix() {

        return r.asDoubleMatrix();
    }

    /**
     * @return the result as an integer array
     */
    public List<String> asStrings() {

        return isNull() ? null : list(r.asStringArray());
    }

    /**
     * @return the result as an integer array
     */
    public List<Integer> asIntegers() {

        int[] array = r.asIntArray();
        return list(list(ArrayUtils.toObject(array)).stream().map(v -> Integer.valueOf(v)));
    }

    /**
     * A universal point for getting value based on its type.
     * 
     * @return The value
     */
    @SuppressWarnings("unchecked")
    public <S> S value() {

        S v = null;
        switch (this.r.getType()) {
            case REXP.XT_ARRAY_STR:
                v = (S) asStrings();
                break;
            case REXP.XT_ARRAY_INT:
                v = (S) asIntegers();
                break;
            case REXP.XT_ARRAY_DOUBLE:
                v = (S) asDecimals();
                break;
            case REXP.XT_DOUBLE:
                v = (S) asDecimal();
                break;
            case REXP.XT_STR:
                v = (S) asString();
                break;
            case REXP.XT_NULL:
                v = null;
                break;
            default:
                throw new ApplicationException("Unsupported R value type (" + this.r.getType() + ")");
        }
        return v;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {

        Object v = (r == null) ? null : value();
        return (v == null) ? //
                "RResult [name=" + name + ", type=null, value=null]" : //
                "RResult [name=" + name + ", type=" + r.getType() + ", value=" + this.value() + "]";
    }

}
