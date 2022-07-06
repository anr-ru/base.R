/*
 * Copyright 2014-2022 the original author or authors.
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * A service of invocation of R scripts under Java/Spring environments.
 *
 * @author Alexey Romanchuk
 * @created Nov 30, 2017
 */
public interface RService {
    /**
     * Evaluates R scripts.
     *
     * @param variables   The map of variables (can be a {@link BigDecimal} value, or a
     *                    list/array of {@link BigDecimal})
     * @param scripts     Names of R files to be executed
     * @param outputNames The names of variables to be read after the execution
     * @return A map with resulted variables
     */
    <S> Map<String, RResult> eval(Map<String, S> variables, List<String> scripts, String... outputNames);
}
