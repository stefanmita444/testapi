package com.example.testapi.services;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestapiApplicationTest {

    
    @Test
    void testRegistration(){
        
        
<<<<<<< HEAD
        JwtResponse jwtResponse = authenticationService.register(signUpDto);

        assertNotNull(jwtResponse);
        assertEquals("token", jwtResponse.getData());
        verify(userRepo).save(any(User.class));
=======
>>>>>>> 9af2b5486fc9e7ac46618e50825da2ff496fce95
        
    }

}
