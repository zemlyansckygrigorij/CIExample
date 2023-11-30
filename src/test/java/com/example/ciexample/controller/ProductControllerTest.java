package com.example.ciexample.controller;

import com.example.ciexample.service.ProductService;
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
    public void sendEmail()  {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("zemlyanscky.grigorij@yandex.ru");
        message.setTo("gr.z.95@mail.ru");
        message.setSubject("Тестовое письмо");
        message.setText("Текстовое сообщение в тестовом письме.\nВторая строка.");
        javaMailSender.send(message)  ;
        assertEquals(1,1);
    }

}






























