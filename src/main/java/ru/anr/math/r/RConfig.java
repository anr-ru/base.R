/**
 * 
 */
package ru.anr.math.r;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * A configuration for used services.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 30, 2017
 *
 */
@Configuration
public class RConfig {

    /**
     * Creation of the bean.
     * 
     * @return A bean instance
     */
    @Bean(name = "rservice")
    public RService bean() {

        return new RServiceImpl();
    }
}
