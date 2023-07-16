package com.onetwoclass.onetwoclass.config.elasticsearch;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.config.ElasticsearchConfigurationSupport;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;

@TestConfiguration
public abstract class AbstractElasticsearchTestConfiguration extends ElasticsearchConfigurationSupport {
  @Bean
  public abstract RestHighLevelClient elasticsearchClient();

  @Bean(name = {"elasticsearchOperations", "elasticsearchTemplate"})
  public ElasticsearchOperations elasticsearchOperations(
      ElasticsearchConverter elasticsearchConverter, RestHighLevelClient restHighLevelClient,
      RestHighLevelClient elasticsearchClient) {

    ElasticsearchRestTemplate template =
        new ElasticsearchRestTemplate(elasticsearchClient, elasticsearchConverter);

    template.setRefreshPolicy(refreshPolicy());

    return template;
  }

}