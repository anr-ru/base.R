/**
 * 
 */
package ru.anr.math.r;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

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
    @Lazy(true)
    public RService bean() {

        return new RServiceImpl();
    }
}
