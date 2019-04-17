package com.bookstore.scraper.Service.impl;

import com.bookstore.scraper.DTO.BookDTO;
import com.bookstore.scraper.Enum.FormatEnum;
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


import java.util.*;

@Service
@Slf4j
public class BookScraperImpl implements BookScraper {

    private static int scrapNum = 10;

    private static int defaultInStock = 100;

    @Override
    public BookDTO scrapOne(String url) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setUrl(url);
        log.info("[info]: processing url {}", url);
        try{
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get();

            if(doc == null){
                throw new ScrapException(ResultEnum.DOC_EMPTY);
            }

            //System.out.println(doc.body());

            Elements checkHardcover = doc.select("ul.a-unordered-list > li.swatchElement > span > span > span.a-button-inner > a");
            if(checkHardcover!=null){
                for(int i = 0;i<checkHardcover.size();i++){
                    Element getHardcover = checkHardcover.get(i);
                    //System.out.println(getHardcover.text());
                    if(getHardcover.text().indexOf("Hardcover")!=-1
                       && getHardcover.attr("href").indexOf("javascript:void(0)")==-1){

                        String hcvUrl = "https://www.amazon.com"+getHardcover.attr("href");
                        //System.out.println("new url: "+hcvUrl);
                        doc = Jsoup.connect(hcvUrl)
                                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                                .referrer("http://www.google.com")
                                .get();
                        bookDTO.setUrl(hcvUrl);
                        break;
                    }
                }
            }

            Element originalPrice = doc.select("span.a-text-strike").first();

            if(originalPrice == null){
                bookDTO.setListPrice(-1);
            }else{
                bookDTO.setListPrice(TransformUtil.extractDouble(originalPrice.text()) );
            }

            Element offerPrice = doc.select("span.offer-price").first();

            if(offerPrice == null){
                offerPrice = doc.selectFirst("span:contains(Special Price:)");
                if(offerPrice != null){
                    bookDTO.setOurPrice(TransformUtil.extractDouble(
                            TransformUtil.removeStr("Special Price:", offerPrice.text())));
                }else{
                    offerPrice = doc.selectFirst("span.a-color-secondary.header-price");
                    if(offerPrice != null){
                        bookDTO.setOurPrice(TransformUtil.extractDouble(offerPrice.text()));
                    }else{
                        throw new ScrapException(ResultEnum.Empty);
                    }

                }

            }else{
                bookDTO.setOurPrice(TransformUtil.extractDouble(offerPrice.text()));
            }



            // set category
            bookDTO.setCategory("Engineering");


            //System.out.println("size: " +doc.select("li:contains(Author:)").size());

            Element author = doc.select("span.author > span.a-declarative > a.a-link-normal").first();
            bookDTO.setAuthor(TransformUtil.removeStr("Author:", author.text()));

            Element publisher = doc.select("li:contains(Publisher:)").first();
            bookDTO.setPublisher(TransformUtil.removeStr("Publisher:", publisher.text()).replaceAll("\\(.*\\)",""));

            String publicationDate = TransformUtil.extractDateFromPublisher(TransformUtil.removeStr("Publisher:", publisher.text()));
            bookDTO.setPublicationDate(publicationDate);

            Element isbn = doc.selectFirst("li:contains(ISBN-10:)");
            bookDTO.setIsbn(TransformUtil.removeStr("ISBN-10:", isbn.text()));

            Element language = doc.selectFirst("li:contains(Language:)");
            bookDTO.setLanguage(TransformUtil.removeStr("Language:", language.text()).toLowerCase());

            Elements bookHeaderInfo = doc.select("h1#title > span");

            // paperback set format and paege number
            if(doc.selectFirst("li:contains("+FormatEnum.PAPERBACK.getMsg()+":)")!=null){
                bookDTO.setFormat(FormatEnum.PAPERBACK.getMsg().toLowerCase());
                Element pages = doc.selectFirst("li:contains("+FormatEnum.PAPERBACK.getMsg()+":)");
                bookDTO.setNumberOfPages(Integer.parseInt(TransformUtil.removeStr(FormatEnum.PAPERBACK.getMsg()+":", pages.text()).split(" ")[0]));
            }else if(doc.selectFirst("li:contains("+FormatEnum.HARDCOVER.getMsg()+":)")!=null){
                bookDTO.setFormat(FormatEnum.HARDCOVER.getMsg().toLowerCase());
                Element pages = doc.selectFirst("li:contains("+FormatEnum.HARDCOVER.getMsg()+":)");
                bookDTO.setNumberOfPages(Integer.parseInt(TransformUtil.removeStr(FormatEnum.HARDCOVER.getMsg()+":", pages.text()).split(" ")[0]));

            }else{
                // kindle
                bookDTO.setFormat(FormatEnum.KINDLE.getMsg().toLowerCase());
                Element pages = doc.selectFirst("li:contains(Print Length:)");
                bookDTO.setNumberOfPages(Integer.parseInt(TransformUtil.removeStr("Print Length:", pages.text()).split(" ")[0]));

            }

            Element bookFormat = bookHeaderInfo.get(1);
            bookDTO.setFormat(bookFormat.text().toLowerCase());

//            Element publishDate = bookHeaderInfo.get(2);
//            String extractDate = publishDate.text().substring(2);
//            bookDTO.setPublicationDate(TransformUtil.extractDate(extractDate));

            //System.out.println(bookFormat.text());
//            Element pages = doc.selectFirst("li:contains("+bookFormat.text()+":)");
//            bookDTO.setNumberOfPages(Integer.parseInt(TransformUtil.removeStr(bookFormat.text()+":", pages.text()).split(" ")[0]));

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
            if(inStock == null){
                bookDTO.setInStockNumber(defaultInStock);
            }else{
                JsonObject obj = new JsonParser().parse(inStock.html()).getAsJsonObject();
                JsonElement inStockNumber = obj.getAsJsonObject("eligibility").get("stockOnHand");
                bookDTO.setInStockNumber(inStockNumber.getAsInt());
            }


            //scrap image, store in aws s3
            Element imgUrl = doc.selectFirst("div#img-canvas > img");
            if(imgUrl == null){
                imgUrl = doc.selectFirst("div#mainImageContainer > img");
            }
            String imgRawUrl = imgUrl.attr("data-a-dynamic-image");
            //System.out.println(imgRawUrl);
            bookDTO.setImgUrl(TransformUtil.extractUrl(imgRawUrl));


        }catch (ScrapException e){
            log.info("[error] parse result error: {}", e.getMsg());
            return null;
        } catch (Exception e){
            e.printStackTrace();
            return null;
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

    @Override
    public List<BookDTO> scrapAll(String url) {
        List<BookDTO> books = new ArrayList<>();
        try{
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get();

            if(doc == null){
                throw new ScrapException(ResultEnum.DOC_EMPTY);
            }

            //System.out.println(doc.body());

            Elements allUrl = doc.select("a.a-link-normal");
            int count = 0;
            Set<String> checkUrl = new HashSet<>();
            for(int i = 0;i<allUrl.size() && count < scrapNum;i++){
                Element eachUrlElem = allUrl.get(i);
                if(eachUrlElem.attr("href").indexOf("product-reviews")!=-1 || checkUrl.contains(eachUrlElem.attr("href"))){
                    continue;
                }
                checkUrl.add(eachUrlElem.attr("href"));
                String eachUrl = "https://www.amazon.com" + eachUrlElem.attr("href");
                BookDTO bookDTO = scrapOne(eachUrl);
                if(bookDTO!=null){
                    bookDTO.setRawUrl(url);
                    books.add(bookDTO);
                    count++;
                }
            }


        }catch (Exception e){
            e.printStackTrace();
        }

        return books;
    }
}
