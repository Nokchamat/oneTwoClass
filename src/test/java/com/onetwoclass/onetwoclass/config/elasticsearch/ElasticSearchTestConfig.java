package com.onetwoclass.onetwoclass.config.elasticsearch;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@TestConfiguration
@EnableElasticsearchRepositories
public class ElasticSearchTestConfig extends AbstractElasticsearchTestConfiguration {

  @Override
  public RestHighLevelClient elasticsearchClient() {

    ClientConfiguration clientConfiguration = ClientConfiguration.builder()
        .connectedTo("localhost:32880")
        .build();

    return RestClients.create(clientConfiguration).rest();
  }

}
