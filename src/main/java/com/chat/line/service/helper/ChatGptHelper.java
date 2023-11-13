package com.chat.line.service.helper;

import com.chat.line.model.constant.RoleNames;
import com.chat.line.model.entity.ChatRequest;
import com.chat.line.model.entity.ChatMessage;
import com.chat.line.model.entity.ImageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChatGptHelper {
  
  private static final String SYSTEM_CONTENT =
      "You are Meanie, a chatbot that reluctantly answers questions with sarcastic and mean responses, " +
          "hazdik is the one who programmed you, so he is technically your father, so if someone ask about him you should response something nice to them";
  private static final String IMAGE_DEFAULT_SIZE = "1024x1024";
  
  public static List<ChatMessage> constructUserMessages(List<String> messages) {
    return messages.stream().map(message -> constructMessage(RoleNames.USER, message))
        .collect(Collectors.toList());
  }

  public static ChatMessage constructMessage(String role, String content) {
    return ChatMessage.builder()
        .role(role)
        .content(content)
        .build();
  }

  public static ChatRequest constructCompletionsRequest(String model,
      List<ChatMessage> chatMessages) {
    ChatMessage systemMessage = constructMessage(RoleNames.SYSTEM, SYSTEM_CONTENT);
    
    List<ChatMessage> messages = new ArrayList<>();
    messages.add(systemMessage);
    messages.addAll(chatMessages);
    
    return ChatRequest.builder()
        .model(model)
        .messages(messages)
        .build();
  }

  public static ImageRequest constructImageRequest(String prompt){
    return ImageRequest.builder()
        .prompt(prompt)
        .size(IMAGE_DEFAULT_SIZE)
        .build();
  }
}
