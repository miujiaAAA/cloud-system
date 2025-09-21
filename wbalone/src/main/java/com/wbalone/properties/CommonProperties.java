package com.wbalone.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * packageName com.wbalone.properties
 *
 * @author lirui
 * @version JDK 17
 * @className CommonProperties
 * @date 2025/9/21
 * @description TODO
 */
@Component
@ConfigurationProperties(prefix = "common")
public class CommonProperties {

    private String timeout;

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }
}
