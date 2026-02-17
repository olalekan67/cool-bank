package com.olalekan.CoolBank.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiResponse <T>{
    private boolean status;
    private String message;
    private T data;

}
