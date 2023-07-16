package com.onetwoclass.onetwoclass.config;


import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;

@TestConfiguration
public class ContainerExtension implements BeforeAllCallback, AfterAllCallback {

  public static GenericContainer<?> esContainer;

  @Override
  public void beforeAll(ExtensionContext context) throws Exception {
    esContainer = new GenericContainer<>(new ImageFromDockerfile().withDockerfileFromBuilder(
        builder -> builder.from("docker.elastic.co/elasticsearch/elasticsearch:7.8.1")
            .run("bin/elasticsearch-plugin", "install", "analysis-nori")
            .build()
    ));

    esContainer
        .withEnv("discovery.type", "single-node")
        .withEnv("http.host", "127.0.0.1")
        .withCreateContainerCmdModifier(command -> {
              HostConfig hostConfig = new HostConfig();
//              hostConfig.withBinds(
//                  new Bind("./dict", new Volume("/usr/share/elasticsearch/config/dict")));
              hostConfig.withPortBindings(
                  new PortBinding(Ports.Binding.bindPort(32880), new ExposedPort(9200)));
              command.withHostConfig(hostConfig);
            }
        );

    esContainer.start();

    String host = String.format("http://%s:%s", esContainer.getContainerIpAddress(),
        esContainer.getMappedPort(9200));

    System.out.println("===================================");
    System.out.println(host); // http://localhost:32880
  }

  @Override
  public void afterAll(ExtensionContext context) {
    esContainer.close();
  }
}
