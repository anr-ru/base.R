/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package ru.anr.math.r;

import org.apache.commons.lang3.ArrayUtils;
import org.rosuda.JRI.REXP;
import ru.anr.base.BaseParent;

import java.math.BigDecimal;
import java.util.List;

/**
 * A simple wrapper around {@link REXP} values.
 *
 * @author Alexey Romanchuk
 * @created Nov 30, 2017
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
     * @param name The name of a variable
     * @param r    The value of the variable
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

        return convert((r == null) ? null : r.asDouble());
    }

    /**
     * @return the result value as a string
     */
    public String asString() {

        return (r == null) ? null : r.asString();
    }

    /**
     * @return the result as a big decimal array
     */
    public List<BigDecimal> asDecimals() {

        double[] array = r.asDoubleArray();
        return list(list(ArrayUtils.toObject(array)).stream().map(this::convert));
    }

    /**
     * Convers a double value to a decimal taking into account Nan or Infinite
     * values undefined for {@link BigDecimal}.
     *
     * @param value the value
     * @return The converted value
     */
    private BigDecimal convert(Double value) {

        return (Double.isNaN(value) || Double.isInfinite(value)) ? null : scale(new BigDecimal(value), 8);
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
        return list(list(ArrayUtils.toObject(array)).stream());
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
                break;
            default:
                v = (S) ("Unsupported R value type (" + this.r.getType() + ")");
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
