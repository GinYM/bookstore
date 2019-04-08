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

  private static String urlAmz = "https://www.amazon.com/Turkeys-Eggcellent-Easter-Turkey-Trouble/dp/154204037X/ref=gbph_img_m-2_82c1_6d7804a7?smid=ATVPDKIKX0DER&pf_rd_p=8e8f1c7a-4944-4873-af6d-7e0003fe82c1&pf_rd_s=merchandised-search-2&pf_rd_t=101&pf_rd_i=45&pf_rd_m=ATVPDKIKX0DER&pf_rd_r=YEEC2ZKFF6YE50SRH66V";


  private static String urlAmz1 = "https://www.amazon.com/dp/1101967358/ref=sspa_dk_detail_4?psc=1";
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

  private String urlKindle = "https://www.amazon.com/Cold-Waters-Normal-Alabama-Book-ebook/dp/B07HF4SCB7/ref=zg_bsnr_283155_1/139-2037907-8740759?_encoding=UTF8&psc=1&refRID=BPQKE6DPM79QNGSQK87P";

  @Test
  public void scrapOneAmz() {
    //String url = "https://www.amazon.com/gp/product/0143132504/ref=s9_acsd_simh_bw_c_x_2_w?pf_rd_m=ATVPDKIKX0DER&pf_rd_s=merchandised-search-10&pf_rd_r=5Y4H05X3EY8T9HV5VFGR&pf_rd_t=101&pf_rd_p=a48686f2-3e3e-43da-91b7-756c8330420e&pf_rd_i=283155";
    BookDTO bookDTO = bookScraper.scrapOne(urlKindle);
    System.out.println(bookDTO);
    System.out.println(bookDTO.getImgUrl());

//    Assert.assertEquals(Double.valueOf(bookDTO.getOurPrice()),Double.valueOf(15.25) );
//    Assert.assertEquals(Double.valueOf(bookDTO.getListPrice()), Double.valueOf(16.95) );
//    Assert.assertEquals(bookDTO.getAuthor(), "Kristen J. Sollee");
//    Assert.assertEquals(bookDTO.getPublisher(),"ThreeL Media");
//    Assert.assertEquals(bookDTO.getIsbn(), 996485279);
//    Assert.assertEquals(bookDTO.getFormat(), "paperback");
//    Assert.assertEquals(Double.valueOf(bookDTO.getShippingWeight()), Double.valueOf(0.45) );
//    Assert.assertEquals(bookDTO.getTitle(), "Feminism & Feminist Theory");
//    Assert.assertEquals(bookDTO.getInStockNumber(), 10);
//    Assert.assertEquals(bookDTO.getPublicationDate(), "2017-06-01");
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

  private static String allUrl = "https://www.amazon.com/b/ref=bhp_brws_awrd?ie=UTF8&node=6960520011&pf_rd_m=ATVPDKIKX0DER&pf_rd_s=merchandised-search-leftnav&pf_rd_r=E54AWBD15ARBVB23CGJR&pf_rd_r=E54AWBD15ARBVB23CGJR&pf_rd_t=101&pf_rd_p=2f0fb499-ec44-47a6-bdfc-74b34c598dbe&pf_rd_p=2f0fb499-ec44-47a6-bdfc-74b34c598dbe&pf_rd_i=283155";

  private String allUrl1 = "https://www.amazon.com/gp/new-releases/books/283155/ref=s9_acsd_ri_bw_clnk/ref=s9_acsd_ri_bw_c_x_ccl_w?pf_rd_m=ATVPDKIKX0DER&pf_rd_s=merchandised-search-11&pf_rd_r=26GMK0YG5W2TGKSE0HQC&pf_rd_t=101&pf_rd_p=c1698c9d-3c53-45d2-b536-8b2e50b21827&pf_rd_i=283155";

  private String allUrl2 = "https://www.amazon.com/b/ref=s9_acss_bw_cg_BHPJAN_1a1_w?node=6960520011&pf_rd_m=ATVPDKIKX0DER&pf_rd_s=merchandised-search-2&pf_rd_r=GA0AR7H02TF3NQCH7H8Q&pf_rd_t=101&pf_rd_p=b7160c47-1f75-45c8-bcb5-7749ef79d02e&pf_rd_i=283155";
  @Test
  public void scrapAll(){
    List<BookDTO> bookDTOS = bookScraper.scrapAll(allUrl2);
    for(BookDTO bookDTO : bookDTOS){
      System.out.println(bookDTO);
    }
  }

}
