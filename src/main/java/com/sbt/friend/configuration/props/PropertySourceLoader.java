package com.sbt.friend.configuration.props;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.ConfigurableEnvironment;

@Configuration
@ComponentScan(basePackages = {"com.sbt.friend.*"})
@PropertySources({
        @PropertySource("classpath:rest.properties")
})
public class PropertySourceLoader {

    @Autowired
    ConfigurableEnvironment env;

    public String getProperty(String key) {
        return env.getProperty(key);
    }
}
