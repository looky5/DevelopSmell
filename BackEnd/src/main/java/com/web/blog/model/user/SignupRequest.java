package com.web.blog.model.user;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Valid
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    @ApiModelProperty(required = true)
    String email;
    @ApiModelProperty(required = true)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d$@$!%*#?&]{8,}$")
    String password;
    String uid;
    String code;
    String salt;
    String introduce;
   
	MultipartFile profile;
    String profilename;
    
    

}
