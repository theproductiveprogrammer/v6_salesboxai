package workflow.engine.conf;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("cadence")
public class CadenceConfig {
    private String host;
    private Integer port;
    private String domain;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
