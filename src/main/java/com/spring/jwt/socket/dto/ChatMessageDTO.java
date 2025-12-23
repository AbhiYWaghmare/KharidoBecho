
package com.spring.jwt.socket.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChatMessageDTO {
    private Long userId;
    private String message;
}
