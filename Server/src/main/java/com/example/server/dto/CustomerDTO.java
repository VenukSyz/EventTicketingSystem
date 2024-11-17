package com.example.server.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CustomerDTO extends UserDTO{
    private boolean vipStatus;
}
