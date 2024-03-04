package kz.aibat.junit5.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class User {
    Integer id;
    String username;
    String password;
}
