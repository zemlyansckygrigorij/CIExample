package com.example.ciexample.controller;

import com.example.ciexample.service.ProductService;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Properties;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {
    @Autowired
    ProductService service;
   // @LocalServerPort
   // private int port;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JavaMailSender javaMailSender;
   // @Autowired
  // private TestRestTemplate restTemplate;
    @Test
    public void commonTest() throws Exception {

        assertEquals(service.getAll().size(),9);
        this.mockMvc.perform(get("http://localhost:8082/product")).andExpect((ResultMatcher) jsonPath("$",hasSize(9)));
        this.mockMvc.perform(get("/product/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Bean")));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/product")
                        .content("fish")
                ).andReturn();
        int idResult = Integer.parseInt(result
                .getResponse()
                .getContentAsString()
                .split(",")[0].split(":")[1]);
        assertEquals(idResult,10);
        this.mockMvc.perform(get("http://localhost:8082/product")).andExpect((ResultMatcher) jsonPath("$",hasSize(10)));
        this.mockMvc.perform(get("/product/"+idResult))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.name", is("fish")));


        mockMvc.perform(MockMvcRequestBuilders
                .put("/product/10")
                .content("ProductTest"))
                .andReturn();
        this.mockMvc.perform(get("http://localhost:8082/product")).andExpect((ResultMatcher) jsonPath("$",hasSize(10)));

      /*  this.mockMvc.perform(get("/product/10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.name", is("ProductTest")));*/
        this.mockMvc.perform(MockMvcRequestBuilders
                .delete("/product/10"));
        this.mockMvc.perform(get("http://localhost:8082/product")).andExpect((ResultMatcher) jsonPath("$",hasSize(9)));
    }
    @Test
    public void sendEmail() throws MessagingException {

       /*
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.mailtrap.io");
        prop.put("mail.smtp.port", "25");
        prop.put("mail.smtp.ssl.trust", "smtp.mailtrap.io");*/
        Properties prop = System.getProperties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.auth", "true");
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("Grig", "Faqwer!2%23g345we!@#");
            }
        });
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("grig71608@gmail.com"));
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse("gr.z.95@mail.ru"));
        message.setSubject("Mail Subject");

        String msg = "This is my first email using JavaMailer";

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        Transport.send(message);
        assertEquals(1,1);
    }

}






























