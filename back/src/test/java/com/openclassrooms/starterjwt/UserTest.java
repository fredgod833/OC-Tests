package com.openclassrooms.starterjwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
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

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class UserTest {

    private static final Logger log = LoggerFactory.getLogger(UserTest.class);

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserMapper userMapper;

    private UserHelper userHelper;

    @Test
    void contextLoads() throws Exception {
        assertThat(this.userRepository).isNotNull();
        assertThat(this.mockMvc).isNotNull();
    }

    @BeforeEach
    public void initTest() {
        userHelper = new UserHelper().initPasswords(passwordEncoder);
        userHelper.initUserMock(userRepository);
    }

    @Test
    public void checkMapperToDto() {

        User user = userHelper.buildNormalUserEntity();
        UserDto userDto = userHelper.buildNormalUserDto();

        assertThat(userMapper.toDto(user)).isEqualTo(userDto);
    }

    @Test
    public void checkMapperNullToDto() {
        assertThat(userMapper.toDto((User) null)).isNull();
        assertThat(userMapper.toDto((List<User>) null)).isNull();
    }

    @Test
    public void checkMapperNullToEntity() {
        assertThat(userMapper.toEntity((UserDto) null)).isNull();
        assertThat(userMapper.toEntity((List<UserDto>) null)).isNull();
    }

    @Test
    public void checkMapperToDtoList() {

        User user = userHelper.buildNormalUserEntity();
        UserDto userDto = userHelper.buildNormalUserDto();

        User admin = userHelper.buildAdminUserEntity();
        UserDto adminDto = userHelper.buildAdminUserDto();

        List<UserDto> result = userMapper.toDto(Arrays.asList(user, admin));

        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(userDto);
        assertThat(result).contains(adminDto);
    }

    @Test
    public void checkMapperToEntityList() {

        User user = userHelper.buildNormalUserEntity();
        UserDto userDto = userHelper.buildNormalUserDto();

        User admin = userHelper.buildAdminUserEntity();
        UserDto adminDto = userHelper.buildAdminUserDto();

        List<User> result = userMapper.toEntity(Arrays.asList(userDto, adminDto));
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(user);
        assertThat(result).contains(admin);
    }

    @Test
    public void checkMapperToEntity() {
        UserHelper userHelper = new UserHelper().initPasswords(passwordEncoder);

        User user = userHelper.buildNormalUserEntity();
        UserDto userDto = userHelper.buildNormalUserDto();

        assertThat(user.equals(userMapper.toEntity(userDto))).isTrue();
    }

    @Test
    @Tag("User") //Ce test fait parti des tests de Users
    public void getUserOk() {

        try {

            UserDto userDto = userHelper.buildNormalUserDto();
            final String bearer = userHelper.getNormalBearerToken(mockMvc);

            final String callResult = this.mockMvc.perform(
                            get("/api/user/1")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(new byte[0])
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();

            ObjectMapper mapper = new ObjectMapper();
            UserDto result = mapper.readValue(callResult, UserDto.class);

            assertThat(result).isEqualTo(userDto);

        } catch (Exception e) {
            log.error("User test error", e);
        }
    }

    @Test
    @Tag("User") //Ce test fait parti des tests de Users
    public void getUserExpiredToken() {
        try {

            final String bearer = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0b3RvQHRpdGkuY29tIiwiaWF0IjoxNzEzOTkwNzIyLCJleHAiOjE3MTQwNzcxMjJ9.mbbjr_KFBEZRawd0EW09OW04LjsJ5xiEurLT_VEOi3UowLcF2usOnGTPmEUQ6j8YWL9KsX5_bmSvVqcBzGJbhg";

            this.mockMvc.perform(
                            get("/api/user/1")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(new byte[0])
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isUnauthorized())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("User test error", e);
        }
    }

    @Test
    @Tag("User") //Ce test fait parti des tests de Users
    public void getUserInvalidToken() {

        try {
            final String bearer = "FooBar";
            this.mockMvc.perform(
                    get("/api/user/1")
                            .header("Authorization", "Bearer " + bearer)
                            .content(new byte[0])
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isUnauthorized())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("User test error", e);
        }

    }

    @Test
    @Tag("User") //Ce test fait parti des tests de Users
    public void getUserWithoutToken() {
        try {
            final String bearer = userHelper.getNormalBearerToken(mockMvc);
            this.mockMvc.perform(
                            get("/api/user/1")
                                    .content(new byte[0])
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isUnauthorized())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("User test error", e);
        }
    }

    @Test
    @Tag("User") //Ce test fait parti des tests de Users
    public void getUserNotFound() {

        try {

            final String bearer = userHelper.getNormalBearerToken(mockMvc);
            this.mockMvc.perform(
                            get("/api/user/3")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(new byte[0])
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isNotFound())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("User test error", e);
        }
    }

    @Test
    @Tag("User") //Ce test fait parti des tests de Users
    public void getUserWithBadId() {
        try {
            final String bearer = userHelper.getNormalBearerToken(mockMvc);
            this.mockMvc.perform(
                            get("/api/user/xx")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(new byte[0])
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("User test error", e);
        }
    }



    @Test
    @Tag("User") //Ce test fait parti des tests de Users
    public void deleteUserOk() {
        try {
            final String bearer = userHelper.getNormalBearerToken(mockMvc);
            this.mockMvc.perform(
                            delete("/api/user/2")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(new byte[0])
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("User test error", e);
        }
    }

    @Test
    @Tag("User") //Ce test fait parti des tests de Users
    public void deleteUserKo() {
        try {
            final String bearer = userHelper.getNormalBearerToken(mockMvc);
            this.mockMvc.perform(
                            delete("/api/user/1")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(new byte[0])
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isUnauthorized())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("User test error", e);
        }
    }

    @Test
    @Tag("User") //Ce test fait parti des tests de Users
    public void deleteUserNotFound() {
        try {
            final String bearer = userHelper.getNormalBearerToken(mockMvc);
            this.mockMvc.perform(
                            delete("/api/user/3")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(new byte[0])
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isNotFound())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("User test error", e);
        }
    }

    @Test
    @Tag("User") //Ce test fait parti des tests de Users
    public void deleteUserBadId() {
        try {
            final String bearer = userHelper.getNormalBearerToken(mockMvc);
            this.mockMvc.perform(
                            delete("/api/user/xx")
                                    .header("Authorization", "Bearer " + bearer)
                                    .content(new byte[0])
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andDo(MockMvcResultHandlers.print());

        } catch (Exception e) {
            log.error("User test error", e);
        }
    }

}
