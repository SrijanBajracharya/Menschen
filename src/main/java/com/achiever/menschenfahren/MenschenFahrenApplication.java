package com.achiever.menschenfahren;

import com.achiever.menschenfahren.service.impl.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = {"com.achiever.menschenfahren"})
@PropertySource(MenschenFahrenApplication.PROPERTY_FILE)
public class MenschenFahrenApplication {

    @Autowired
    EmailServiceImpl emailService;

    public static final String PROPERTY_FILE = "classpath:application.properties";

    public static void main(String[] args) {
        SpringApplication.run(MenschenFahrenApplication.class, args);
    }

    @Bean
    public void sendEmails() {

        System.out.println("Sending Email...");

        emailService.sendSimpleMessage("melognadnile@gmail.com", "test", "hello there");
        //sendEmailWithAttachment();
//
//		} catch (MessagingException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

        System.out.println("Done");

    }
}
