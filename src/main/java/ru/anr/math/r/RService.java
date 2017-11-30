/**
 * 
 */
package ru.anr.math.r;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * A service of invocation of R scripts under Java/Spring environments.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 30, 2017
 *
 */
public interface RService {

    /**
     * Evaluation of R scripts.
     * 
     * @param variables
     *            A map of variables (can be a {@link BigDecimal} value, or a
     *            list/array of {@link BigDecimal})
     * @param scripts
     *            Names of R files to be executed
     * @param outputNames
     *            The names of variables to be read after the execution
     * @return A map with resulted variables
     */
    <S> Map<String, RResult> eval(Map<String, S> variables, List<String> scripts, String... outputNames);
}
