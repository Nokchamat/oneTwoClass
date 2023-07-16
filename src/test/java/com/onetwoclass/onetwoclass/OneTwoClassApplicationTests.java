package com.onetwoclass.onetwoclass;

import com.onetwoclass.onetwoclass.config.elasticsearch.ElasticTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(ElasticTestContainer.class)
class OneTwoClassApplicationTests {

  @Test
  void contextLoads() {
  }

}
