/**
 * 
 */
package ru.anr.math.cfd;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import ru.anr.base.tests.BaseTestCase;
import ru.anr.math.r.RResult;
import ru.anr.math.r.RService;

/**
 * Tests for the {@link RService} service.
 *
 *
 * @author
 * @created Sep 14, 2017
 *
 */
@ContextConfiguration(locations = { "classpath:tests-context.xml" }, inheritLocations = false)
public class RTest extends BaseTestCase {

    /**
     * {@link RService}
     */
    @Autowired
    private RService service;

    /**
     * A list of procedures
     */
    private static final String[] FUNCS = { "test-definitions.R", "test-functions.R", "test-main.R" };

    /**
     * Use case: evaluate a set of scripts passing them variables and reading
     * proper results
     */
    @Test
    public void main() {

        /*
         * 1. Input variables: a single value, arrays of string or decimals
         */
        Map<String, Object> inputs = toMap("x", d(12.45), //
                "arr", list(d("3.14"), d("2.72")), //
                "str", list("Good", "So-So", "Bad"), //
                "dx", list(d("1"), d("2")));

        /*
         * 2. Invocations: all provided functions are just combined to a single
         * script before invocation. We should also specify output variables to
         * have them in the resulted map.
         */
        Map<String, RResult> rs = service.eval(inputs, list(FUNCS), //
                "rs", "pp", "sx", "sxx", "out", "str", "dx", "dd", "globb", "stored");

        /*
         * 3. Check outputs
         */

        // 3.1 A single result - an internal variable or array
        Assert.assertEquals(d("186.75000000"), rs.get("rs").asDecimal());
        // NB: by default it's one element array.
        Assert.assertEquals(list(d("186.75000000")), rs.get("rs").value());

        Assert.assertEquals(d("5.86000000"), rs.get("out").asDecimal());
        Assert.assertEquals(//
                list(d("1.00000000"), d("2.00000000"), d("3.00000000"), d("4.00000000"), d("5.00000000")),
                rs.get("pp").asDecimals());
        Assert.assertEquals(//
                list(d("1.00000000"), d("2.00000000"), d("3.00000000"), d("4.00000000"), d("5.00000000")),
                rs.get("pp").value());

        // 3.2 String arrays
        Assert.assertEquals(list("1", "2", "3"), rs.get("sx").asStrings());
        Assert.assertEquals(list("1", "2", "3"), rs.get("sx").value());
        Assert.assertEquals(list("1", "2", "3"), rs.get("sxx").asStrings());

        /*
         * 3.3 Delivered parameters are changed in global scope only
         */
        Assert.assertEquals(list("Good", "Hm-Hm", "Not Bad"), rs.get("str").asStrings());
        Assert.assertEquals(list("Good", "Hm-Hm", "Not Bad"), rs.get("str").value());

        /*
         * 3.4 But also a global assign operator <<- works inside of locally
         * defined functions.
         */
        Assert.assertEquals(list(d("1.00000000"), d("200.00000000")), rs.get("dx").asDecimals());
        Assert.assertEquals(list(d("1.00000000"), d("100.00000000")), rs.get("dd").asDecimals());

        Assert.assertEquals(list(d("1.00000000"), d("100.00000000")), rs.get("globb").asDecimals());

        // 3.5 Stored value
        Assert.assertEquals(d("24.00000000"), rs.get("stored").asDecimal());

        /*
         * 4. Check the second invocation
         */
        rs = service.eval(inputs, list(FUNCS), "rs", "pp", "sx", "sxx", "out", "str", "dx", "dd", "globb", "stored");

        // 4.1 Stored value is not changed between invocations
        Assert.assertEquals(d("24.00000000"), rs.get("stored").asDecimal());
    }
}
