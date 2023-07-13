package com.onetwoclass.onetwoclass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
public class OneTwoClassApplication {

  public static void main(String[] args) {
    SpringApplication.run(OneTwoClassApplication.class, args);
  }

}
