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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class RegisterTest {

    private static final Logger log = LoggerFactory.getLogger(RegisterTest.class);

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private AuthController authController;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() throws Exception {
        assertThat(this.userRepository).isNotNull();
        assertThat(this.mockMvc).isNotNull();
        assertThat(this.authController).isNotNull();
    }

    @BeforeEach
    public void init() {

        User totoUser = new User();
        totoUser.setEmail("toto@titi.com");
        totoUser.setPassword(passwordEncoder.encode("123456"));
        totoUser.setFirstName("Toto");
        totoUser.setLastName("Titi");
        totoUser.setAdmin(true);

        when(userRepository.findByEmail("toto@titi.com")).thenReturn(Optional.of(totoUser));
        when(userRepository.existsByEmail("toto@titi.com")).thenReturn(true);

        User tataUser = new User();
        tataUser.setEmail("tata@titi.com");
        tataUser.setPassword(passwordEncoder.encode("123456"));
        tataUser.setFirstName("Tata");
        tataUser.setLastName("Titi");
        tataUser.setAdmin(false);

        //intercept save operation to avoid bdd injection
        when(userRepository.save(any())).thenReturn(tataUser);

    }

    @Test
    @Tag("Register") // ce test fait parti des tests de register
    public void registerOk() {

        final String registerForm="{\n" +
                "\"email\":\"tata@titi.com\"," +
                "\"firstName\":\"Tata\"," +
                "\"lastName\":\"Titi\"," +
                "\"password\":\"123456\"\n" +
                "}";

        try {
            this.mockMvc.perform(
                    post("/api/auth/register")
                            .content(registerForm)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("Registration test error", e);
        }

    }

    @Test
    @Tag("Register") // ce test fait parti des tests de register
    public void alreadyRegistered() {

        final String registerForm="{\n" +
                "\"email\":\"toto@titi.com\"," +
                "\"firstName\":\"Toto\"," +
                "\"lastName\":\"Titi\"," +
                "\"password\":\"123456\"\n" +
                "}";

        try {
            this.mockMvc.perform(
                            post("/api/auth/register")
                                    .content(registerForm)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("Registration test error", e);

        }

    }


    @Test
    @Tag("Register") // ce test fait parti des tests de register
    public void missingEmail() {

        final String registerForm="{\n" +
                "\"email\":\"\"," +
                "\"firstName\":\"Toto\"," +
                "\"lastName\":\"Titi\"," +
                "\"password\":\"12345\"\n" +
                "}";

        try {
            this.mockMvc.perform(
                            post("/api/auth/register")
                                    .content(registerForm)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("Registration test error", e);
        }

    }


    @Test
    @Tag("Register") // ce test fait parti des tests de register
    public void badEmailFormat() {

        final String registerForm="{\n" +
                "\"email\":\"toto$titi.com\"," +
                "\"firstName\":\"Toto\"," +
                "\"lastName\":\"Titi\"," +
                "\"password\":\"12345\"\n" +
                "}";

        try {
            this.mockMvc.perform(
                            post("/api/auth/register")
                                    .content(registerForm)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("Registration test error", e);
        }

    }

    @Test
    @Tag("Register") // ce test fait parti des tests de register
    public void noFirstName() {

        final String registerForm="{\n" +
                "\"email\":\"tata@titi.com\"," +
                "\"firstName\":\"\"," +
                "\"lastName\":\"Titi\"," +
                "\"password\":\"12345\"\n" +
                "}";

        try {
            this.mockMvc.perform(
                            post("/api/auth/register")
                                    .content(registerForm)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("Registration test error", e);
        }

    }

    @Test
    @Tag("Register") // ce test fait parti des tests de register
    public void noLastName() {

        final String registerForm="{\n" +
                "\"email\":\"tata@titi.com\"," +
                "\"firstName\":\"Tata\"," +
                "\"lastName\":\"\"," +
                "\"password\":\"12345\"\n" +
                "}";

        try {
            this.mockMvc.perform(
                            post("/api/auth/register")
                                    .content(registerForm)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("Registration test error", e);
        }

    }

    @Test
    @Tag("Register") // ce test fait parti des tests de register
    public void noPassword() {

        final String registerForm="{\n" +
                "\"email\":\"tata@titi.com\"," +
                "\"firstName\":\"Tata\"," +
                "\"lastName\":\"Titi\"," +
                "\"password\":\"\"\n" +
                "}";

        try {
            this.mockMvc.perform(
                            post("/api/auth/register")
                                    .content(registerForm)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("Registration test error", e);
        }

    }

}
