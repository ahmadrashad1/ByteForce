package com.SportSync2.dto;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {
    private boolean success;
    private String message;
}


