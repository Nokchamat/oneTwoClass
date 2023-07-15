package com.onetwoclass.onetwoclass.config;

import com.onetwoclass.onetwoclass.config.elasticsearch.AbstractElasticsearchConfiguration;
import com.onetwoclass.onetwoclass.repository.DayClassSearchRepository;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;

//@Import(ElasticTestContainer.class) 테스트 파일에 넣기

@TestConfiguration
@EnableElasticsearchRepositories(basePackageClasses = DayClassSearchRepository.class)
public class ElasticTestContainer extends AbstractElasticsearchConfiguration {

  private static final String ELASTICSEARCH_VERSION = "7.15.2";
  private static final DockerImageName ELASTICSEARCH_IMAGE =
      DockerImageName
          .parse("docker.elastic.co/elasticsearch/elasticsearch")
          .withTag(ELASTICSEARCH_VERSION);
  private static final ElasticsearchContainer container;

  // testContainer 띄우기
  static {
    container = new ElasticsearchContainer(ELASTICSEARCH_IMAGE);
    container.start();
  }

  // 띄운 컨테이너로 ESCilent 재정의
  @Override
  public RestHighLevelClient elasticsearchClient() {
    ClientConfiguration clientConfiguration = ClientConfiguration.builder()
        .connectedTo(container.getHttpHostAddress())
        .build();
    return RestClients.create(clientConfiguration).rest();
  }
}
