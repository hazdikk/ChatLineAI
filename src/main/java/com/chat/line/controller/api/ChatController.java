package com.chat.line.controller.api;

import com.chat.line.model.constant.ModelNames;
import com.chat.line.service.api.BotService;
import com.chat.line.service.api.TextProcessingService;
import com.chat.line.service.helper.ChatHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

  private final BotService botService;
  private final TextProcessingService textProcessingService;
  private final Map<String, List<String>> conversationsByName;

  @GetMapping(value = "/get-keywords")
  public ResponseEntity<String> getKeywords(@RequestParam String username) {
    username = ChatHelper.constructWebSourceId(username);
    List<String> recentConversations = conversationsByName.get(username);
    List<String> keywords =
        this.textProcessingService.getKeywordsFromConversations(recentConversations);
    return new ResponseEntity<>(keywords.toString(), HttpStatus.OK);
  }

  @PostMapping(value = "/chat")
  public ResponseEntity<String> chat(@RequestParam String username, @RequestParam String message)
      throws JsonProcessingException {
    username = ChatHelper.constructWebSourceId(username);
    String botResponse =
        this.botService.handleTextContentAndReturnResponse(ModelNames.DEFAULT, message, username);
    return new ResponseEntity<>(botResponse, HttpStatus.OK);
  }
}
