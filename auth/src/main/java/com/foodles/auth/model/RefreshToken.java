package com.foodles.auth.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.*;

import java.time.Instant;

@Document
@Builder
@Getter
@Setter
public class RefreshToken {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    @Indexed
    private String token;
    private Instant expiry;
    @DBRef(lazy = true)
    @Field(value = "userId")
    private User user;
}
