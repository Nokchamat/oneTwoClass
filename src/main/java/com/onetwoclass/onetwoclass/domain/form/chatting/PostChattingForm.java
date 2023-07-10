package com.onetwoclass.onetwoclass.domain.form.chatting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostChattingForm {

  private Long chattingRoomId;

  private String text;

}
