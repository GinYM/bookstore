package com.bookstore.scraper.Util;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class TransformUtilTest {

  @Test
  public void extractDouble() {
    String data = "my price is: $15.1";
    Double result = 0.0;
    try{
      result = TransformUtil.extractDouble(data);

    }catch (Exception e){
      e.printStackTrace();
    }
    Assert.assertEquals(Double.valueOf(15.1), result);
  }

  @Test
  public void removeStr(){
    String result = TransformUtil.removeStr("Author:", "  Author: Hello World!  ");
    //System.out.println(result);
    Assert.assertEquals("Hello World!", result);
  }

  @Test
  public void extractInteger() throws Exception{
    Integer result = TransformUtil.extractInteger("wkejjkngeroi456eger4");
    System.out.println(result);
    Assert.assertEquals(result, Integer.valueOf(456));
  }

  @Test
  public void extractDate() throws Exception {
    String result = TransformUtil.extractDate("SEPTEMBER 2015");
    System.out.println(result);
  }

  @Test
  public void extractUrl(){
    String rawUrl =
        "{\"https://images-na.ssl-images-amazon.com/images/I/51vr9rmhSNL._SX258_BO1,204,203,200_.jpg\":[260,260],\"https://images-na.ssl-images-amazon.com/images/I/51vr9rmhSNL._SY498_BO1,204,203,200_.jpg\":[500,500]}";
    String url = TransformUtil.extractUrl(rawUrl);
    System.out.println(url);
    Assert.assertEquals(url, "https://images-na.ssl-images-amazon.com/images/I/51vr9rmhSNL._SY498_BO1,204,203,200_.jpg");
  }

  @Test
  public void extractFromPublisher() throws Exception{
    String publisher = "Liveright; 1 edition (March 20, 2018)";
    System.out.println(TransformUtil.extractDateFromPublisher(publisher));
  }
}
