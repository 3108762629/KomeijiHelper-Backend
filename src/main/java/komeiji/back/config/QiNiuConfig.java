package komeiji.back.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix="qiniu")
public class QiNiuConfig {
    private String accessKey;
    private String secretKey;
    private String bucket;
    private String domain;
}
