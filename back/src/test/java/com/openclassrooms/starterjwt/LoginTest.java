package com.openclassrooms.starterjwt;

import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class LoginTest {

    private static final Logger log = LoggerFactory.getLogger(LoginTest.class);

    @MockBean
    private UserRepository userRepository;

    @InjectMocks
    private AuthController authController;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() throws Exception {
        assertThat(this.passwordEncoder).isNotNull();
        assertThat(this.userRepository).isNotNull();
        assertThat(this.mockMvc).isNotNull();
        assertThat(this.authController).isNotNull();
    }

    @BeforeEach
    public void init() {
        User resultUser = new User();
        resultUser.setEmail("cristina.c@yoga.com");
        resultUser.setPassword(passwordEncoder.encode("123456"));
        resultUser.setFirstName("Cristina");
        resultUser.setLastName("Cordula");
        resultUser.setAdmin(true);
        resultUser.setId(1L);

        when(userRepository.findByEmail("cristina.c@yoga.com")).thenReturn(Optional.of(resultUser));
        when(userRepository.existsByEmail("cristina.c@yoga.com")).thenReturn(true);
    }

    @Test
    public void checkUserDetails() throws Exception {
        UserDetails ud1 = userDetailsService.loadUserByUsername("cristina.c@yoga.com");
        UserDetails ud2 = userDetailsService.loadUserByUsername("cristina.c@yoga.com");
        assertThat(ud1).isEqualTo(ud1);
        assertThat(ud1).isEqualTo(ud2);
        assertThat(ud1).isNotEqualTo(null);
    }

    @Test
    @Tag("Login") //Ce test fait parti des tests de login
    public void loginOK() {

        final String loginOK="{\n" +
                "\"email\":\"cristina.c@yoga.com\"," +
                "\"password\":\"123456\"\n" +
                "}";

        try {
            this.mockMvc.perform(
                    post("/api/auth/login")
                            .content(loginOK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("Login test error", e);
        }

    }

    @Test
    @Tag("Login") //Ce test fait parti des tests de login
    public void loginWrongUserName() {
        final String login="{\n" +
                "\"email\":\"cristina.a@yoga.com\"," +
                "\"password\":\"123456\"\n" +
                "}";
        try {
            this.mockMvc.perform(
                    post("/api/auth/login")
                            .content(login)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isUnauthorized())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("Login test error", e);
        }
    }

    @Test
    @Tag("Login") //Ce test fait parti des tests de login
    public void loginWrongUserPassword() {
        final String login="{\n" +
                "\"email\":\"cristina.c@yoga.com\"," +
                "\"password\":\"12345\"\n" +
                "}";
        try {
            this.mockMvc.perform(
                    post("/api/auth/login")
                            .content(login)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isUnauthorized())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("Login test error", e);
        }
    }

    @Test
    @Tag("Login") //Ce test fait parti des tests de login
    public void loginMissingUserName() {
        final String loginOK="{\n" +
                "\"email\":\"\"," +
                "\"password\":\"123456\"\n" +
                "}";

        try {
            this.mockMvc.perform(
                    post("/api/auth/login")
                            .content(loginOK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isBadRequest())
                    .andDo(MockMvcResultHandlers.print());;

        } catch (Exception e) {
            log.error("Login test error", e);
        }
    }

    @Test
    @Tag("Login") //Ce test fait parti des tests de login
    public void loginMissingPassword() {
        final String loginOK="{\n" +
                "\"email\":\"cristina.c@yoga.com\"," +
                "\"password\":\"\"\n" +
                "}";

        try {
            this.mockMvc.perform(
                    post("/api/auth/login")
                            .content(loginOK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isBadRequest());

        } catch (Exception e) {
            log.error("Login test error", e);
        }
    }

}
