/**
 * 
 */
package ru.anr.math.r;

import java.math.BigDecimal;

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
}
