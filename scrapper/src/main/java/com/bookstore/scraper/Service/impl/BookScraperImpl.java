package com.bookstore.scraper.Service.impl;

import com.bookstore.scraper.DTO.BookDTO;
import com.bookstore.scraper.Enum.ResultEnum;
import com.bookstore.scraper.Exception.ScrapException;
import com.bookstore.scraper.Service.BookScraper;
import com.bookstore.scraper.Util.TransformUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
        bookDTO.setUrl(url);
        try{
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get();

            if(doc == null){
                throw new ScrapException(ResultEnum.DOC_EMPTY);
            }

            //System.out.println(doc.body());

            Element originalPrice = doc.select("span.a-text-strike").first();

            if(originalPrice == null){
                bookDTO.setListPrice(-1);
            }else{
                bookDTO.setListPrice(TransformUtil.extractDouble(originalPrice.text()) );
            }

            Element offerPrice = doc.select("span.offer-price").first();

            if(offerPrice == null){
                throw new ScrapException(ResultEnum.Empty);
            }

            bookDTO.setOurPrice(TransformUtil.extractDouble(offerPrice.text()));

            // set category
            bookDTO.setCategory("Engineering");


            //System.out.println("size: " +doc.select("li:contains(Author:)").size());

            Element author = doc.select("span.author > span.a-declarative > a.a-link-normal").first();
            bookDTO.setAuthor(TransformUtil.removeStr("Author:", author.text()));

            Element publisher = doc.select("li:contains(Publisher:)").first();
            bookDTO.setPublisher(TransformUtil.removeStr("Publisher:", publisher.text()).replaceAll("\\(.*\\)",""));

            Element isbn = doc.selectFirst("li:contains(ISBN-10:)");
            bookDTO.setIsbn(TransformUtil.removeStr("ISBN-10:", isbn.text()));

            Element language = doc.selectFirst("li:contains(Language:)");
            bookDTO.setLanguage(TransformUtil.removeStr("Language:", language.text()).toLowerCase());

            Elements bookHeaderInfo = doc.select("h1#title > span");

            Element bookFormat = bookHeaderInfo.get(1);
            bookDTO.setFormat(bookFormat.text().toLowerCase());

            Element publishDate = bookHeaderInfo.get(2);
            String extractDate = publishDate.text().substring(2);
            bookDTO.setPublicationDate(TransformUtil.extractDate(extractDate));

            Element pages = doc.selectFirst("li:contains(Hardcover:)");
            bookDTO.setNumberOfPages(Integer.parseInt(TransformUtil.removeStr("Hardcover:", pages.text()).split(" ")[0]));

            Element weight = doc.selectFirst("li:contains(Shipping Weight:)");
            if(weight==null){
                bookDTO.setShippingWeight(-1);
            }else{
                bookDTO.setShippingWeight(Double.parseDouble(TransformUtil.removeStr("Shipping Weight:", weight.text()).split(" ")[0]));
            }

            Element title = doc.selectFirst("span#productTitle");
            bookDTO.setTitle(title.text());


            Element desc = doc.selectFirst("div#bookDescription_feature_div > noscript");
            bookDTO.setDescription(desc.text());

            Element inStock = doc.selectFirst("div#turboState > script");
            //Element inStock = doc.selectFirst("div.a-button-stack > div#buyNow");
            //System.out.println(inStock.html());
            JsonObject obj = new JsonParser().parse(inStock.html()).getAsJsonObject();
            //System.out.println(obj);
            JsonElement inStockNumber = obj.getAsJsonObject("eligibility").get("stockOnHand");
            bookDTO.setInStockNumber(inStockNumber.getAsInt());




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
