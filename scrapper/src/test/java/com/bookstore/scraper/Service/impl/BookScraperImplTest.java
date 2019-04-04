package com.bookstore.scraper.Service.impl;

import com.bookstore.scraper.DTO.BookDTO;
import com.bookstore.scraper.Service.BookScraper;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.netflix.discovery.converters.Auto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BookScraperImplTest {

  @Autowired
  private BookScraper bookScraper;

  private static String url = "https://www.strandbooks.com/staff-picks/witches-sluts-feminists-conjuring-the-sex-positive";

  @Test
  public void scrapOne() {
    //String url = "https://www.amazon.com/gp/product/0143132504/ref=s9_acsd_simh_bw_c_x_2_w?pf_rd_m=ATVPDKIKX0DER&pf_rd_s=merchandised-search-10&pf_rd_r=5Y4H05X3EY8T9HV5VFGR&pf_rd_t=101&pf_rd_p=a48686f2-3e3e-43da-91b7-756c8330420e&pf_rd_i=283155";
    BookDTO bookDTO = bookScraper.scrapOne(url);
    System.out.println(bookDTO);

    Assert.assertEquals(Double.valueOf(bookDTO.getOurPrice()),Double.valueOf(15.25) );
    Assert.assertEquals(Double.valueOf(bookDTO.getListPrice()), Double.valueOf(16.95) );
    Assert.assertEquals(bookDTO.getAuthor(), "Kristen J. Sollee");
    Assert.assertEquals(bookDTO.getPublisher(),"ThreeL Media");
    Assert.assertEquals(bookDTO.getIsbn(), 996485279);
    Assert.assertEquals(bookDTO.getFormat(), "paperback");
    Assert.assertEquals(Double.valueOf(bookDTO.getShippingWeight()), Double.valueOf(0.45) );
    Assert.assertEquals(bookDTO.getTitle(), "Feminism & Feminist Theory");
    Assert.assertEquals(bookDTO.getInStockNumber(), 10);
    Assert.assertEquals(bookDTO.getPublicationDate(), "2017-06-01");
  }

  @Test
  public void scrapList() {
    List<String> urls = Arrays.asList(url, url);
    List<BookDTO> result = bookScraper.scrapList(urls);

    for(BookDTO bookDTO : result){
      Assert.assertEquals(Double.valueOf(bookDTO.getOurPrice()),Double.valueOf(15.25) );
      Assert.assertEquals(Double.valueOf(bookDTO.getListPrice()), Double.valueOf(16.95) );
      Assert.assertEquals(bookDTO.getAuthor(), "Kristen J. Sollee");
      Assert.assertEquals(bookDTO.getPublisher(),"ThreeL Media");
      Assert.assertEquals(bookDTO.getIsbn(), 996485279);
      Assert.assertEquals(bookDTO.getFormat(), "paperback");
      Assert.assertEquals(Double.valueOf(bookDTO.getShippingWeight()), Double.valueOf(0.45) );
      Assert.assertEquals(bookDTO.getTitle(), "Feminism & Feminist Theory");
      Assert.assertEquals(bookDTO.getInStockNumber(), 10);
      Assert.assertEquals(bookDTO.getPublicationDate(), "2017-06-01");
    }

  }
}
