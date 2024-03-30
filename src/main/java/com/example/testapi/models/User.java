package com.example.testapi.models;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class User implements UserDetails {

    @Id
    private String id;
    
    @NotNull(message = "First name cant be blank!")
    private String firstName;

    @NotNull(message = "Last name cant be blank!")
    private String lastName;

    @NotNull(message = "Username can't be blank")
    @Size(min = 8, max = 20, message = "Username must be greater than 8 characters")
    private String username;

    @NotNull(message = "Password cant be blank")
    @Size(min = 8, message = "Password must be greater than 9 characters")
    private String password;

    @NotNull(message = "Email cant be null")
    @Email(message = "Email must follow email formatting")
    private String email;

    private Date purity;

    private String expoPushToken;

    private Date dob;
    private String picture;

    public List<String> friends;

    private Role role ;

    public void addFriend(String username) {
        this.friends.add(username);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null) {
            return List.of(new SimpleGrantedAuthority(Role.USER.toString()));  // or return a default role if desired
        }
        return List.of(new SimpleGrantedAuthority(role.name()));
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
