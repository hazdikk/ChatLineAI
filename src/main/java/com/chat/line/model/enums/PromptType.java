package com.chat.line.model.enums;

import lombok.Getter;

@Getter
public enum PromptType {

  GPT_4("!gpt4"),
  CREATE_IMAGE("!image"),
  KEYWORD("!keyword"),
  DEFAULT("!");

  private final String value;

  PromptType(String value) {
    this.value = value;
  }

  public static PromptType getPromptTypeOrDefault(String input) {
    if (input.startsWith(GPT_4.value)) {
      return GPT_4;
    } else if (input.startsWith(CREATE_IMAGE.value)) {
      return CREATE_IMAGE;
    } else if (input.startsWith(KEYWORD.value)) {
      return KEYWORD;
    } else {
      return DEFAULT;
    }
  }

}
