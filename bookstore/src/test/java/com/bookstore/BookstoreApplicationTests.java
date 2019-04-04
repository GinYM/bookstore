package com.bookstore;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import javax.mail.internet.MimeMessage;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookstoreApplicationTests {

    @Autowired
    private JavaMailSender mailSender;

    @Test
    public void contextLoads() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        String pwd1 = encoder.encode("demo");
        String pwd2 = encoder.encode("demo");
        System.out.println(pwd1+" "+pwd2);
        if(encoder.matches("demo", pwd1)){
            System.out.println("match!!");
        }
    }

    @Test
    public void mailSender(){
        MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
                email.setTo("565953583@qq.com");
                email.setSubject("Order Confirmation - ");
                email.setText("hehe", true);
                email.setFrom("ecommercebookstore@gmail.com");
            }
        };
    }


}

