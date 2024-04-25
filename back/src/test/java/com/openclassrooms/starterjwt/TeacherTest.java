package com.openclassrooms.starterjwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class TeacherTest {

    private static final Logger log = LoggerFactory.getLogger(TeacherTest.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherController teacherController;

    @Autowired
    private MockMvc mockMvc;

    private static Teacher teacher1;
    private static Teacher teacher2;

    private static TeacherDto teacherDto1;

    private static TeacherDto teacherDto2;

    private UserHelper userHelper;

    @Autowired
    private TeacherMapper teacherMapper;


    @Test
    void contextLoads() throws Exception {
        assertThat(this.teacherRepository).isNotNull();
        assertThat(this.teacherController).isNotNull();
        assertThat(this.mockMvc).isNotNull();
    }

    @BeforeAll
    public static void initDatas() {
        // Avec constructeur vide
        teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setLastName("Cordula");
        teacher1.setFirstName("Cristina");
        teacher1.setCreatedAt(LocalDateTime.of(1988,8,8, 10,8,10));
        teacher1.setUpdatedAt(LocalDateTime.of(1992,9,9, 12,13,25));

        teacherDto1 = new TeacherDto();
        teacherDto1.setId(1L);
        teacherDto1.setLastName("Cordula");
        teacherDto1.setFirstName("Cristina");
        teacherDto1.setCreatedAt(LocalDateTime.of(1988,8,8, 10,8,10));
        teacherDto1.setUpdatedAt(LocalDateTime.of(1992,9,9, 12,13,25));

        // Avec constructeur plein
        teacher2 = new Teacher(
                2L,
                "Aguilera",
                "Cristina",
                LocalDateTime.of(1965,6,6, 10,6,37),
                LocalDateTime.of(1993,11,11, 13,13,46)
        );

        teacherDto2 = new TeacherDto(
                2L,
                "Aguilera",
                "Cristina",
                LocalDateTime.of(1965,6,6, 10,6,37),
                LocalDateTime.of(1993,11,11, 13,13,46)
        );

    }

    @BeforeEach
    public void initTest() {

        userHelper = new UserHelper().initPasswords(passwordEncoder);
        userHelper.initUserMock(userRepository);

        List<Teacher> teacherList = Arrays.asList(teacher1,teacher2);
        when(teacherRepository.findAll()).thenReturn(teacherList);

        when(teacherRepository.getById(1L)).thenReturn(teacher1);
        when(teacherRepository.getById(2L)).thenReturn(teacher2);
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher1));
        when(teacherRepository.findById(2L)).thenReturn(Optional.of(teacher2));

    }


    @Test
    public void checkMapperToDto() {
        assertThat(teacherMapper.toDto(teacher1)).isEqualTo(teacherDto1);
        assertThat(teacherMapper.toDto(teacher2)).isEqualTo(teacherDto2);
    }

    @Test
    public void checkMapperToEntity() {
        assertThat(teacherMapper.toEntity(teacherDto1 )).isEqualTo(teacher1);
        assertThat(teacherMapper.toEntity(teacherDto2)).isEqualTo(teacher2 );
    }

    @Test
    public void checkMapperNullToDto() {
        assertThat(teacherMapper.toDto((Teacher) null)).isNull();
        assertThat(teacherMapper.toDto((List<Teacher>) null)).isNull();
    }

    @Test
    public void checkMapperNullToEntity() {
        assertThat(teacherMapper.toEntity((TeacherDto) null)).isNull();
        assertThat(teacherMapper.toEntity((List<TeacherDto>) null)).isNull();
    }

    @Test
    public void checkMapperToDtoList() {
        List<TeacherDto> result = teacherMapper.toDto(Arrays.asList(teacher1, teacher2));
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(teacherDto1);
        assertThat(result).contains(teacherDto2);
    }

    @Test
    public void checkMapperToEntityList() {
        List<Teacher> result = teacherMapper.toEntity(Arrays.asList(teacherDto1, teacherDto2));
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(teacher1);
        assertThat(result).contains(teacher2);
    }

    @Test
    @Tag("Teachers") //Ce test fait parti des tests de teacher
    public void getTeacherOk() {

        try {
            String bearer = userHelper.getNormalBearerToken(mockMvc);
            final String callResult = this.mockMvc.perform(
                    get("/api/teacher/1")
                            .header("Authorization", "Bearer " + bearer)
                            .content(new byte[0])
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();

            ObjectMapper mapper = new ObjectMapper();
            TeacherDto result = mapper.readValue(callResult, TeacherDto.class);
            assertThat(result).isEqualTo(teacherDto1);

        } catch (Exception e) {
            log.error("Teacher test error", e);
        }

    }

    @Test
    @Tag("Teachers") //Ce test fait parti des tests de teacher
    public void teacherNotFound() {

        try {

            String bearer = userHelper.getNormalBearerToken(mockMvc);

            this.mockMvc.perform(
                            get("/api/teacher/0")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(new byte[0])
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isNotFound())
                    .andDo(MockMvcResultHandlers.print());


        } catch (Exception e) {
            log.error("Teacher test error", e);
        }

    }

    @Test
    @Tag("Teachers") //Ce test fait parti des tests de teacher
    public void teacherIdInvalid() {

        try {

            String bearer = userHelper.getNormalBearerToken(mockMvc);

            this.mockMvc.perform(
                            get("/api/teacher/xy")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(new byte[0])
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("Teacher test error", e);
        }

    }

    @Test
    @Tag("Teachers") //Ce test fait parti des tests de teacher
    public void findAll() {

        try {

            String bearer = userHelper.getNormalBearerToken(mockMvc);
            final String callResult = this.mockMvc.perform(
                                    get("/api/teacher")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(new byte[0])
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();

            ObjectMapper om = new ObjectMapper();
            List<Object> mappedList = om.readValue(callResult, ArrayList.class);
            assertThat(mappedList.size()).isEqualTo(2);
            assertThat(mappedList).contains(om.writeValueAsString(teacherDto1));
            assertThat(mappedList).contains(om.writeValueAsString(teacherDto2));

        } catch (Exception e) {
            log.error("Teacher test error", e);
        }

    }

}
