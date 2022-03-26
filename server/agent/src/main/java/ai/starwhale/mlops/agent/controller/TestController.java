package ai.starwhale.mlops.agent.controller;

import cn.hutool.core.io.IoUtil;
import com.github.dockerjava.transport.DockerHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;

import static com.github.dockerjava.transport.DockerHttpClient.Request.Method;
import static com.github.dockerjava.transport.DockerHttpClient.Request.builder;

@RestController("/test")
public class TestController {

    @Autowired
    private DockerHttpClient httpClient;

    @GetMapping(value = "/ping")
    public ResponseEntity<String> ping() {
        DockerHttpClient.Request request = builder()
                .method(Method.GET)
                .path("/_ping")
                .build();

        try (DockerHttpClient.Response response = httpClient.execute(request)) {
            String body = IoUtil.read(response.getBody(), Charset.defaultCharset());
            return ResponseEntity.status(response.getStatusCode()).body(body);
        }
    }
}
