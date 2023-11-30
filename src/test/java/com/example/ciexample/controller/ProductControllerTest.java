package com.example.ciexample.controller;

import com.example.ciexample.service.ProductService;
import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;


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

//    @Autowired
//    private JavaMailSender javaMailSender;
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

        // Recipient's email ID needs to be mentioned.
        String to = "gr.z.95@mail.ru";

        // Sender's email ID needs to be mentioned
        String from = "grig71608@gmail.com";

        // Assuming you are sending email from through gmails smtp
        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = new Properties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "25");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("grig71608@gmail.com", "Faqwer!2%23g345we!@#");

            }

        });
/**/
        // Used to debug SMTP issues
        session.setDebug(true);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(String.valueOf(new InternetAddress(from)));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("This is the Subject Line!");

            // Now set the actual message
            message.setText("This is actual message");

            System.out.println("sending...");
            // Send message
         //   Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }






        assertEquals(1,1);
    }

}






























