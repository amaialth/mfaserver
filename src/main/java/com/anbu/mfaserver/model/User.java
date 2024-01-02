package com.anbu.mfaserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document(collection = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    @Id
    private String id;
    @Indexed
    @NotBlank
    private String username;
    private String password;
    boolean mfaEnabled;
    @JsonIgnore
    private String secretKey;
    private String firstName;
    private String lastName;
    private boolean active;
    private boolean accountVerified;
    @DBRef
    private Set<Role> roles = new HashSet<>();
}
