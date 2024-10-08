package com.ewallet.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailsObject {
    private String name;
    private String username;
    private String email;
}
