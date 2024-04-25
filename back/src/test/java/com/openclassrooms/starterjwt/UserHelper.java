package com.openclassrooms.starterjwt;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Classe utilitaire de construction des objets Mockés
 */
public class UserHelper {

    private static final String USER_TEST_PASSWORD = "12345";
    private static final String ADMIN_TEST_PASSWORD = "nimda";

    private static final String USER_TEST_EMAIL = "cristina@yoga.com";
    private static final String ADMIN_TEST_EMAIL = "admin@yoga.com";

    private String encodedUserPwd;
    private String encodedAdminPwd;

    UserHelper() {}

    /**
     * Encodage des mots de passe une seule fois pour éviter les incohérences entre entity et Dto si utilisation de sel d'encodage.
     * @param passwordEncoder
     * @return
     */
    public UserHelper initPasswords( PasswordEncoder passwordEncoder ) {
        this.encodedUserPwd = passwordEncoder.encode(USER_TEST_PASSWORD);
        this.encodedAdminPwd = passwordEncoder.encode(ADMIN_TEST_PASSWORD);
        return this;
    }

    public User buildAdminUserEntity() {
        User result =  new User(
                ADMIN_TEST_EMAIL,
                "Administator",
                "The",
                encodedAdminPwd,
                true);
        result.setId(1L);
        result.setCreatedAt(LocalDateTime.of(1965,6,6, 10,6,37));
        result.setUpdatedAt(LocalDateTime.of(1993,11,11, 13,13,46));
        return result;
    }

    public UserDto buildAdminUserDto() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail(ADMIN_TEST_EMAIL);
        userDto.setLastName("Administator");
        userDto.setFirstName("The");
        userDto.setPassword(encodedAdminPwd);
        userDto.setAdmin(true);
        userDto.setCreatedAt(LocalDateTime.of(1965,6,6, 10,6,37));
        userDto.setUpdatedAt(LocalDateTime.of(1993,11,11, 13,13,46));
        return userDto;
    }

    public UserDto buildNormalUserDto() {
        return new UserDto(
                2L,
                USER_TEST_EMAIL,
                "Aguilera",
                "Cristina",
                false,
                encodedUserPwd,
                LocalDateTime.of(1965,6,6, 10,6,37),
                LocalDateTime.of(1993,11,11, 13,13,46)
        );
    }

    public User buildNormalUserEntity() {
        User user = new User();
        user.setId(2L);
        user.setEmail(USER_TEST_EMAIL);
        user.setLastName("Aguilera");
        user.setFirstName("Cristina");
        user.setPassword(encodedUserPwd);
        user.setAdmin(false);
        user.setCreatedAt(LocalDateTime.of(1965,6,6, 10,6,37));
        user.setUpdatedAt(LocalDateTime.of(1993,11,11, 13,13,46));
        return user;
    }

    /**
     * Appelles le login et retourne un bearer token jwt valide
     * @param mockMvc
     * @param password
     * @return
     */
    private static String getBearerToken(MockMvc mockMvc, final String email, final String password) {

        final String login="{\n" +
                "\"email\":\"" + email + "\",\n" +
                "\"password\":\"" + password + "\"\n" +
                "}";

        try {
            final String tokenResponse = mockMvc.perform(
                            post("/api/auth/login")
                                    .content(login)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

            JacksonJsonParser jsonParser = new JacksonJsonParser();
            return jsonParser.parseMap(tokenResponse).get("token").toString();

        } catch (Exception e) {
            return null;
        }

    }

    String getNormalBearerToken(MockMvc mockMvc) {
        return getBearerToken(mockMvc, USER_TEST_EMAIL, USER_TEST_PASSWORD );
    }

    String getAdminBearerToken(MockMvc mockMvc) {
        return getBearerToken(mockMvc, ADMIN_TEST_EMAIL, ADMIN_TEST_PASSWORD );
    }

    void initUserMock(UserRepository userRepository) {

        User adminUser = buildAdminUserEntity();
        when(userRepository.findById(adminUser.getId())).thenReturn(Optional.of(adminUser));
        when(userRepository.findByEmail(adminUser.getEmail())).thenReturn(Optional.of(adminUser));
        when(userRepository.existsByEmail(adminUser.getEmail())).thenReturn(true);

        User normalUser = buildNormalUserEntity();
        when(userRepository.findById(normalUser.getId())).thenReturn(Optional.of(normalUser));
        when(userRepository.findByEmail(normalUser.getEmail())).thenReturn(Optional.of(normalUser));
        when(userRepository.existsByEmail(normalUser.getEmail())).thenReturn(true);

        when(userRepository.findAll()).thenReturn(Arrays.asList(adminUser,normalUser));

    }

}
