package com.example.demo.DTO.changePassword;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class ChangePasswordDto {

    private String oldPassword;
    private String newPassword;
    private String ConformPassword;


}
