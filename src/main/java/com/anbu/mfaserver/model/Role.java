package com.anbu.mfaserver.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collation = "roles")
@Data
public class Role {
    @Id
    private String id;
    private ERole role;

}
