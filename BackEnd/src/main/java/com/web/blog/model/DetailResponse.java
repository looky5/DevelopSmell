package com.web.blog.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
public class DetailResponse {

	    @ApiModelProperty(value = "status", position = 1)
	    public boolean status;
	    @ApiModelProperty(value = "data", position = 2)
	    public String post;
	    @ApiModelProperty(value = "object", position = 3)
	    public Object commnets;
	
}
