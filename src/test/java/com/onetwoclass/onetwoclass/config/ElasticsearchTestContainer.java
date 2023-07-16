//package com.onetwoclass.onetwoclass.config;
//
//@Configuration("elasticsearchTestContainer")
//@Profile(CygnusCoreConfig.PROFILE_TEST)
//public class ElasticsearchTestContainer {
//
//  private final ElasticsearchContainer container;
//  private final RestHighLevelClient client;
//  private static final String ELASTICSEARCH = "elasticsearch";
//  private static final String DOCKER_IMAGE_NAME = "docker.elastic.co/elasticsearch/elasticsearch:7.9.3";
//
//  public ElasticsearchTestContainer() {
//    container = new ElasticsearchContainer(DOCKER_IMAGE_NAME);
//    container.start();
//    this.client = createRestHighLevelClient();
//  }
//
//  public RestHighLevelClient getClient() {
//    return client;
//  }
//
//  @PreDestroy
//  public void destroy() {
//    container.stop();
//  }
//
//  private RestHighLevelClient createRestHighLevelClient() { ...}
//
//  private BasicCredentialsProvider getBasicCredentialsProvider() {...}
//}f
