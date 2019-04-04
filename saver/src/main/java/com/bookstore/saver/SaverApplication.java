package com.bookstore.saver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class SaverApplication {

  public static void main(String[] args) {
    SpringApplication.run(SaverApplication.class, args);
  }
}
