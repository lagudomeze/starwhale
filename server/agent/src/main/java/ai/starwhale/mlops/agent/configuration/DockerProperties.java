package ai.starwhale.mlops.agent.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "sw.agent.docker")
public class DockerProperties {
    private String host;
}
