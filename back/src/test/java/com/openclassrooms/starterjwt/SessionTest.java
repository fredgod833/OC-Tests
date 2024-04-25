package com.openclassrooms.starterjwt;

import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.mapper.SessionMapperImpl;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.SessionRepository;
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

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class SessionTest {

    private static final Logger log = LoggerFactory.getLogger(SessionTest.class);

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private SessionRepository sessionRepository;

    @InjectMocks
    private SessionController sessionController;

    @Autowired
    private MockMvc mockMvc;

    private UserHelper userHelper;

    @Autowired
    private SessionMapper sessionMapper;


    private Session firstSession;
    private SessionDto firstSessionDto;
    @Autowired
    private SessionMapperImpl sessionMapperImpl;


    @Test
    void contextLoads() throws Exception {
        assertThat(this.userRepository).isNotNull();
        assertThat(this.mockMvc).isNotNull();    }

    @BeforeEach
    public void initTest() {

        userHelper = new UserHelper().initPasswords(passwordEncoder);
        userHelper.initUserMock(userRepository);

        firstSession = new Session();
        firstSession.setId(1L);
        firstSession.setCreatedAt(LocalDateTime.of(1965,6,6, 10,6,37));
        firstSession.setUpdatedAt(LocalDateTime.of(1993,11,11, 13,13,46));
        firstSession.setUsers(new ArrayList<>());
        firstSession.getUsers().add(userHelper.buildNormalUserEntity());
        firstSession.setName("Test Session");
        firstSession.setDate(new Date());
        firstSession.setDescription("Mocked Session number 1");
        firstSession.setTeacher(new Teacher());
        firstSession.getTeacher().setId(1L);

        firstSessionDto = new SessionDto();
        firstSessionDto.setId(1L);
        firstSessionDto.setCreatedAt(LocalDateTime.of(1965,6,6, 10,6,37));
        firstSessionDto.setUpdatedAt(LocalDateTime.of(1993,11,11, 13,13,46));
        firstSessionDto.setUsers(new ArrayList<>());
        firstSessionDto.getUsers().add(2L);
        firstSessionDto.setName("Test Session");
        firstSessionDto.setDate(new Date());
        firstSessionDto.setDescription("Mocked Session number 1");
        firstSessionDto.setTeacher_id(1L);

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(firstSession));

    }

    @Test
    public void checkMapperToDto() {
        assertThat(sessionMapper.toDto(firstSession)).isEqualTo(firstSessionDto);
        List<SessionDto> list = sessionMapper.toDto(Arrays.asList(firstSession));
        assertThat(list).hasSize(1);
        assertThat(list).contains(firstSessionDto);
    }

    @Test
    public void checkMapperToEntity() {
        assertThat(sessionMapper.toEntity(firstSessionDto )).isEqualTo(firstSession);
        List<Session> list = sessionMapper.toEntity(Arrays.asList(firstSessionDto));
        assertThat(list).hasSize(1);
        assertThat(list).contains(firstSession);
    }

    @Test
    public void checkMapperNullToDto() {
        assertThat(sessionMapper.toDto((Session) null)).isNull();
        assertThat(sessionMapper.toDto((List<Session>) null)).isNull();
    }

    @Test
    public void checkMapperNullToEntity() {
        assertThat(sessionMapper.toEntity((SessionDto) null)).isNull();
        assertThat(sessionMapper.toEntity((List<SessionDto>) null)).isNull();
    }


    @Test
    @Tag("Session") //Ce test fait parti des tests de Session
    public void createSessionOk() {
        try {

            final String json = "{\n" +
                    "  \"name\":\"Test Complet\",\n" +
                    "  \"date\":\"2050-07-03\",\n"+
                    "  \"teacher_id\":1,\n" +
                    "  \"description\":\"Test de creation de Session Passant\"\n" +
                    "}";

            String bearer = userHelper.getNormalBearerToken(mockMvc);
            this.mockMvc.perform(
                            post("/api/session")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(json)
                                    .accept(MediaType.APPLICATION_JSON_VALUE)
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("Session test error", e);

        }
    }

    @Test
    @Tag("Session") //Ce test fait parti des tests de Session
    public void createSessionNoName() {
        try {

            final String json = "{\n" +
                    "  \"date\":\"2050-07-03\",\n"+
                    "  \"teacher_id\":1,\n" +
                    "  \"description\":\"Test de creation de Session sans nom\"\n" +
                    "}";

            String bearer = userHelper.getNormalBearerToken(mockMvc);
            this.mockMvc.perform(
                            post("/api/session")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(json)
                                    .accept(MediaType.APPLICATION_JSON_VALUE)
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("Session test error", e);

        }
    }

    @Test
    @Tag("Session") //Ce test fait parti des tests de Session
    public void createSessionNoDate() {
        try {

            final String json = "{\n" +
                    "  \"name\":\"Test Complet\",\n" +
                    "  \"teacher_id\":1,\n" +
                    "  \"description\":\"Test de creation de Session sans nom\"\n" +
                    "}";

            String bearer = userHelper.getNormalBearerToken(mockMvc);
            this.mockMvc.perform(
                            post("/api/session")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(json)
                                    .accept(MediaType.APPLICATION_JSON_VALUE)
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("Session test error", e);

        }
    }

    @Test
    @Tag("Session") //Ce test fait parti des tests de Session
    public void createSessionNoTeacher() {
        try {

            final String json = "{\n" +
                    "  \"name\":\"Test Complet\",\n" +
                    "  \"date\":\"2050-07-03\",\n"+
                    "  \"description\":\"Test de creation de Session sans prof\"\n" +
                    "}";

            String bearer = userHelper.getNormalBearerToken(mockMvc);
            this.mockMvc.perform(
                            post("/api/session")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(json)
                                    .accept(MediaType.APPLICATION_JSON_VALUE)
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("Session test error", e);

        }
    }

    @Test
    @Tag("Session") //Ce test fait parti des tests de Session
    public void createSessionNoDescription() {
        try {

            final String json = "{\n" +
                    "  \"name\":\"Test Complet\",\n" +
                    "  \"date\":\"2050-07-03\",\n"+
                    "  \"teacher_id\":1,\n" +
                    "}";

            String bearer = userHelper.getNormalBearerToken(mockMvc);
            this.mockMvc.perform(
                            post("/api/session")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(json)
                                    .accept(MediaType.APPLICATION_JSON_VALUE)
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("Session test error", e);

        }
    }

    @Test
    @Tag("Session") //Ce test fait parti des tests de Session
    public void updateSessionOk() {
        try {

            final String json = "{\n" +
                    "  \"name\":\"Test Complet\",\n" +
                    "  \"date\":\"2050-07-03\",\n"+
                    "  \"teacher_id\":1,\n" +
                    "  \"description\":\"Test de creation de Session Passant\"\n" +
                    "}";

            String bearer = userHelper.getNormalBearerToken(mockMvc);
            this.mockMvc.perform(
                            put("/api/session/1")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(json)
                                    .accept(MediaType.APPLICATION_JSON_VALUE)
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("Session test error", e);

        }
    }

    @Test
    @Tag("Session") //Ce test fait parti des tests de Session
    public void updateSessionInvalidId() {
        try {

            final String json = "{\n" +
                    "  \"name\":\"Test Complet\",\n" +
                    "  \"date\":\"2050-07-03\",\n"+
                    "  \"teacher_id\":1,\n" +
                    "  \"description\":\"Test de creation de Session Passant\"\n" +
                    "}";

            String bearer = userHelper.getNormalBearerToken(mockMvc);
            this.mockMvc.perform(
                            put("/api/session/2x")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(json)
                                    .accept(MediaType.APPLICATION_JSON_VALUE)
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("Session test error", e);

        }
    }

    @Test
    @Tag("Session") //Ce test fait parti des tests de Session
    public void updateSessionNoName() {
        try {

            final String json = "{\n" +
                    "  \"date\":\"2050-07-03\",\n"+
                    "  \"teacher_id\":1,\n" +
                    "  \"description\":\"Test de creation de Session sans nom\"\n" +
                    "}";

            String bearer = userHelper.getNormalBearerToken(mockMvc);
            this.mockMvc.perform(
                            put("/api/session/1")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(json)
                                    .accept(MediaType.APPLICATION_JSON_VALUE)
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("Session test error", e);

        }
    }

    @Test
    @Tag("Session") //Ce test fait parti des tests de Session
    public void updateSessionNoDate() {
        try {

            final String json = "{\n" +
                    "  \"name\":\"Test Complet\",\n" +
                    "  \"teacher_id\":1,\n" +
                    "  \"description\":\"Test de creation de Session sans nom\"\n" +
                    "}";

            String bearer = userHelper.getNormalBearerToken(mockMvc);
            this.mockMvc.perform(
                            put("/api/session/1")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(json)
                                    .accept(MediaType.APPLICATION_JSON_VALUE)
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("Session test error", e);

        }
    }

    @Test
    @Tag("Session") //Ce test fait parti des tests de Session
    public void updateSessionNoTeacher() {
        try {

            final String json = "{\n" +
                    "  \"name\":\"Test Complet\",\n" +
                    "  \"date\":\"2050-07-03\",\n"+
                    "  \"description\":\"Test de creation de Session sans prof\"\n" +
                    "}";

            String bearer = userHelper.getNormalBearerToken(mockMvc);
            this.mockMvc.perform(
                            put("/api/session/1")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(json)
                                    .accept(MediaType.APPLICATION_JSON_VALUE)
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("Session test error", e);

        }
    }

    @Test
    @Tag("Session") //Ce test fait parti des tests de Session
    public void updateSessionNoDescription() {
        try {

            final String json = "{\n" +
                    "  \"name\":\"Test Complet\",\n" +
                    "  \"date\":\"2050-07-03\",\n"+
                    "  \"teacher_id\":1,\n" +
                    "}";

            String bearer = userHelper.getNormalBearerToken(mockMvc);
            this.mockMvc.perform(
                            put("/api/session/1")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(json)
                                    .accept(MediaType.APPLICATION_JSON_VALUE)
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("Session test error", e);

        }
    }

    @Test
    @Tag("Session") //Ce test fait parti des tests de Session
    public void deleteSessionOk() {
        try {
            String bearer = userHelper.getNormalBearerToken(mockMvc);
            this.mockMvc.perform(
                            delete("/api/session/1")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(new byte[0])
                    )
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print());
            verify(sessionRepository).deleteById(1L);
        } catch (Exception e) {
            log.error("Session test error", e);

        }
    }

    @Test
    @Tag("Session") //Ce test fait parti des tests de Session
    public void deleteSessionBadId() {
        try {
            String bearer = userHelper.getNormalBearerToken(mockMvc);
            this.mockMvc.perform(
                            delete("/api/session/2")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(new byte[0])
                    )
                    .andExpect(status().isNotFound())
                    .andDo(MockMvcResultHandlers.print());
        } catch (Exception e) {
            log.error("Session test error", e);

        }
    }

    @Test
    @Tag("Session") //Ce test fait parti des tests de Session
    public void deleteSessionInvalidId() {
        try {
            String bearer = userHelper.getNormalBearerToken(mockMvc);
            this.mockMvc.perform(
                            delete("/api/session/xx")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(new byte[0])
                    )
                    .andExpect(status().isBadRequest())
                    .andDo(MockMvcResultHandlers.print());
        } catch (Exception e) {
            log.error("Session test error", e);

        }
    }

    @Test
    @Tag("Session") //Ce test fait parti des tests de Session
    public void getSessionOk() {
        try {
            String bearer = userHelper.getNormalBearerToken(mockMvc);
            this.mockMvc.perform(
                            get("/api/session/1")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(new byte[0])
                    )
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print());
        } catch (Exception e) {
            log.error("Session test error", e);

        }
    }

    @Test
    @Tag("Session") //Ce test fait parti des tests de Session
    public void getSessionBadId() {
        try {
            String bearer = userHelper.getNormalBearerToken(mockMvc);
            this.mockMvc.perform(
                            get("/api/session/2")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(new byte[0])
                    )
                    .andExpect(status().isNotFound())
                    .andDo(MockMvcResultHandlers.print());
        } catch (Exception e) {
            log.error("Session test error", e);

        }
    }

    @Test
    @Tag("Session") //Ce test fait parti des tests de Session
    public void getSessionInvalidId() {

        try {
            String bearer = userHelper.getNormalBearerToken(mockMvc);
            this.mockMvc.perform(
                            get("/api/session/ed")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(new byte[0])
                    )
                    .andExpect(status().isBadRequest())
                    .andDo(MockMvcResultHandlers.print());
        } catch (Exception e) {
            log.error("Session test error", e);

        }

    }

    @Test
    @Tag("Session") //Ce test fait parti des tests de Session
    public void getAllSessionOk() {

        try {
            String bearer = userHelper.getNormalBearerToken(mockMvc);
            this.mockMvc.perform(
                            get("/api/session")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(new byte[0])
                    )
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("Session test error", e);

        }

    }

    @Test
    @Tag("Session") //Ce test fait parti des tests de Session
    public void participateSessionOk() {

        try {
            String bearer = userHelper.getNormalBearerToken(mockMvc);
            this.mockMvc.perform(
                            post("/api/session/1/participate/1")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(new byte[0])
                    )
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("Session test error", e);

        }

    }

    @Test
    @Tag("Session") //Ce test fait parti des tests de Session
    public void participateSessionAlready() {

        try {
            String bearer = userHelper.getNormalBearerToken(mockMvc);
            this.mockMvc.perform(
                            post("/api/session/1/participate/2")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(new byte[0])
                    )
                    .andExpect(status().isBadRequest())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("Session test error", e);

        }

    }

    @Test
    @Tag("Session") //Ce test fait parti des tests de Session
    public void participateSessionBadSessionId() {

        try {
            String bearer = userHelper.getNormalBearerToken(mockMvc);
            this.mockMvc.perform(
                            post("/api/session/0/participate/1")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(new byte[0])
                    )
                    .andExpect(status().isNotFound())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("Session test error", e);

        }

    }

    @Test
    @Tag("Session") //Ce test fait parti des tests de Session
    public void participateSessionBadUserId() {

        try {
            String bearer = userHelper.getNormalBearerToken(mockMvc);
            this.mockMvc.perform(
                            post("/api/session/1/participate/0")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(new byte[0])
                    )
                    .andExpect(status().isNotFound())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("Session test error", e);

        }

    }

    @Test
    @Tag("Session") //Ce test fait parti des tests de Session
    public void participateSessionInvalidSessionId() {

        try {
            String bearer = userHelper.getNormalBearerToken(mockMvc);
            this.mockMvc.perform(
                            post("/api/session/dz/participate/2")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(new byte[0])
                    )
                    .andExpect(status().isBadRequest())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("Session test error", e);

        }

    }

    @Test
    @Tag("Session") //Ce test fait parti des tests de Session
    public void participateSessionInvalidUserId() {

        try {
            String bearer = userHelper.getNormalBearerToken(mockMvc);
            this.mockMvc.perform(
                            post("/api/session/1/participate/fsf")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(new byte[0])
                    )
                    .andExpect(status().isBadRequest())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("Session test error", e);

        }

    }



    @Test
    @Tag("Session") //Ce test fait parti des tests de Session
    public void noParticipateSessionOk() {

        try {
            String bearer = userHelper.getNormalBearerToken(mockMvc);
            this.mockMvc.perform(
                            delete("/api/session/1/participate/2")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(new byte[0])
                    )
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("Session test error", e);

        }

    }

    @Test
    @Tag("Session") //Ce test fait parti des tests de Session
    public void noParticipateSessionBadSessionId() {

        try {
            String bearer = userHelper.getNormalBearerToken(mockMvc);
            this.mockMvc.perform(
                            delete("/api/session/0/participate/2")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(new byte[0])
                    )
                    .andExpect(status().isNotFound())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("Session test error", e);

        }

    }

    @Test
    @Tag("Session") //Ce test fait parti des tests de Session
    public void noParticipateSessionBadUserId() {

        try {
            String bearer = userHelper.getNormalBearerToken(mockMvc);
            this.mockMvc.perform(
                            delete("/api/session/1/participate/0")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(new byte[0])
                    )
                    .andExpect(status().isBadRequest())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("Session test error", e);

        }

    }

    @Test
    @Tag("Session") //Ce test fait parti des tests de Session
    public void noParticipateSessionInvalidSessionId() {

        try {
            String bearer = userHelper.getNormalBearerToken(mockMvc);
            this.mockMvc.perform(
                            delete("/api/session/dz/participate/2")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(new byte[0])
                    )
                    .andExpect(status().isBadRequest())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("Session test error", e);

        }

    }

    @Test
    @Tag("Session") //Ce test fait parti des tests de Session
    public void noParticipateSessionInvalidUserId() {

        try {
            String bearer = userHelper.getNormalBearerToken(mockMvc);
            this.mockMvc.perform(
                            delete("/api/session/1/participate/fsf")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(new byte[0])
                    )
                    .andExpect(status().isBadRequest())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("Session test error", e);

        }

    }



}
