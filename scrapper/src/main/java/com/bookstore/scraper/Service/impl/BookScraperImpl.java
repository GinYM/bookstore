package com.bookstore.scraper.Service.impl;

import com.bookstore.scraper.DTO.BookDTO;
import com.bookstore.scraper.Enum.ResultEnum;
import com.bookstore.scraper.Exception.ScrapException;
import com.bookstore.scraper.Service.BookScraper;
import com.bookstore.scraper.Util.TransformUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class BookScraperImpl implements BookScraper {
    @Override
    public BookDTO scrapOne(String url) {
        BookDTO bookDTO = new BookDTO();
        try{
            Document doc = Jsoup.connect(url).get();

            if(doc == null){
                throw new ScrapException(ResultEnum.DOC_EMPTY);
            }

            Element originalPrice = doc.select("div.price-original").first();

            if(originalPrice == null){
                bookDTO.setListPrice(-1);
            }else{
                bookDTO.setListPrice(TransformUtil.extractDouble(originalPrice.text()) );
            }

            Element offerPrice = doc.select("div.price-sale").first();

            if(offerPrice == null){
                throw new ScrapException(ResultEnum.Empty);
            }

            // language set to english
            bookDTO.setLanguage("english");
            bookDTO.setCategory("Engineering");

            bookDTO.setOurPrice(TransformUtil.extractDouble(offerPrice.text()));


            //System.out.println("size: " +doc.select("li:contains(Author:)").size());

            Element author = doc.select("li:contains(Author:)").first();
            bookDTO.setAuthor(TransformUtil.removeStr("Author:", author.text()));

            Element publisher = doc.select("li:contains(Publisher:)").first();
            bookDTO.setPublisher(TransformUtil.removeStr("Publisher:", publisher.text()));

            Element isbn = doc.selectFirst("li:contains(ISBN-10:)");
            bookDTO.setIsbn(TransformUtil.removeStr("ISBN-10:", isbn.text()));

            Element bookFormat = doc.selectFirst("li:contains(Format:)");
            bookDTO.setFormat(TransformUtil.removeStr("Format:", bookFormat.text()).toLowerCase());

            Element weight = doc.selectFirst("li:contains(Weight:)");
            if(weight==null){
                bookDTO.setShippingWeight(-1);
            }else{
                bookDTO.setShippingWeight(Double.parseDouble(TransformUtil.removeStr("Weight:", weight.text())));
            }

            Element title = doc.selectFirst("li:contains(Subject:)");
            if(title!=null){
                bookDTO.setTitle(TransformUtil.removeStr("Subject:", title.text()));
            }else{
                title = doc.selectFirst("h1.product-detail__name");
                if(title!=null){
                    bookDTO.setTitle(title.text());
                }
            }


            Element desc = doc.selectFirst("[name='abstract']");
            bookDTO.setDescription(desc.attr("content"));

            Element inStock = doc.selectFirst("div.skus__quantity");
            bookDTO.setInStockNumber(TransformUtil.extractInteger(inStock.text()));

            Element publishDate = doc.selectFirst("li:contains(Published:)");
            String extractDate = TransformUtil.removeStr("Published:", publishDate.text());
            bookDTO.setPublicationDate(TransformUtil.extractDate(extractDate));

        }catch (Exception e){
            e.printStackTrace();
        }

        return bookDTO;

    }

    @Override
    public List<BookDTO> scrapList(List<String> urls) {
        List<BookDTO> result = new ArrayList<>();
        for(String url : urls){
            result.add(scrapOne(url));
        }
        return result;
    }
}
