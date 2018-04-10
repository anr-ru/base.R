/**
 * 
 */
package ru.anr.math.cfd;

import java.math.BigDecimal;
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

        Map<String, RResult> rs = service.eval(toMap("x", d(12.45)), list(FUNCS), "rs", "pp", "sx", "sxx");
        Assert.assertEquals(d("186.75"), rs.get("rs").asDecimal());

        Assert.assertArrayEquals(new BigDecimal[]{ d("1"), d("2"), d("3"), d("4"), d("5") },
                rs.get("pp").asDecimals());

        Assert.assertArrayEquals(new String[]{ "1", "2", "3" }, rs.get("sx").asStrings());
        Assert.assertArrayEquals(new String[]{ "1", "2", "3" }, rs.get("sxx").asStrings());
    }
}
