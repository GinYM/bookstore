package com.bookstore.service.impl;

import com.bookstore.service.AmazonS3ClientService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AmazonClientTest {

  @Autowired
  private AmazonS3ClientService amazonClient;

  @Test
  public void uploadFile() throws Exception {
    String name = "4.png";
    File img = new File("src/main/resources/static/image/book/" + name);
    if(img == null){
      System.out.println("empty!!!");
      throw new Exception("error");
    }
    amazonClient.uploadSingleFileToS3Bucket(img, true, name);
  }

  @Test
  public void deleteFileFromS3Bucket() {
    amazonClient.deleteFileFromS3Bucket("4.png");
  }
}
