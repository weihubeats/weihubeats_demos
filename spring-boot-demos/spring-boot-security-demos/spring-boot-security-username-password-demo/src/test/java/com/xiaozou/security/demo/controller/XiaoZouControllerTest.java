package com.xiaozou.security.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author : wh
 * @date : 2025/2/19 17:15
 * @description:
 */
@SpringBootTest
@AutoConfigureMockMvc
class XiaoZouControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void publicAccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/public"))
            .andExpect(status().isOk());
    }

    @Test
    void adminAccessDeniedWithoutAuth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/dashboard"))
            .andExpect(status().is3xxRedirection());
    }

    @Test
    void userLoginSuccess() throws Exception {
        mockMvc.perform(formLogin("/login")
                .user("username", "user")
                .password("password", "123456"))
            .andExpect(redirectedUrl("/home"));
    }
}