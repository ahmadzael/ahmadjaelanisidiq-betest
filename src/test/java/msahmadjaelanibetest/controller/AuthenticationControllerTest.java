package msahmadjaelanibetest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import msahmadjaelanibetest.model.LoginRequest;
import msahmadjaelanibetest.model.RegisterRequest;
import msahmadjaelanibetest.model.TokenResponse;
import msahmadjaelanibetest.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRegister() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password");
        registerRequest.setUsername("testuser");
        registerRequest.setFullName("Test User");
        registerRequest.setAccountNumber("123456");
        registerRequest.setRegistrationNumber("REG123");

        when(authenticationService.register(any(RegisterRequest.class))).thenReturn("OK");

        ResponseEntity<String> responseEntity = authenticationController.register(registerRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("OK", responseEntity.getBody());
    }



    @Test
    public void testRegisterWithMissingFields() {
        RegisterRequest registerRequest = new RegisterRequest();
        // Missing required fields

        when(authenticationService.register(any(RegisterRequest.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required fields"));

        try {
            authenticationController.register(registerRequest);
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            assertEquals("Missing required fields", e.getReason());
        }
    }

    @Test
    public void testAuthenticate() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");

        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setToken("fake-jwt-token");

        when(authenticationService.login(any(LoginRequest.class))).thenReturn(tokenResponse);

        ResponseEntity<TokenResponse> responseEntity = authenticationController.authenticate(loginRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("fake-jwt-token", responseEntity.getBody().getToken());
    }

    @Test
    public void testAuthenticateWithInvalidCredentials() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("invaliduser");
        loginRequest.setPassword("wrongpassword");

        when(authenticationService.login(any(LoginRequest.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username Or Password wrong"));

        try {
            authenticationController.authenticate(loginRequest);
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.UNAUTHORIZED, e.getStatusCode());
            assertEquals("Username Or Password wrong", e.getReason());
        }
    }

}
