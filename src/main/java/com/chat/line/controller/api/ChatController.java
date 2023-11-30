package com.chat.line.controller.api;

import com.chat.line.model.constant.ModelNames;
import com.chat.line.model.rest.MessageRequest;
import com.chat.line.service.api.BotService;
import com.chat.line.service.api.TextProcessingService;
import com.chat.line.service.helper.ChatGptHelper;
import com.chat.line.service.helper.ChatHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;
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
    List<String> keywords = this.textProcessingService.getKeywordsFromUsername(username);

    return ResponseEntity.ok(String.join(",", keywords));
  }

  @PostMapping(value = "/chat")
  public ResponseEntity<String> chat(@RequestParam String username,
      @RequestBody MessageRequest request) throws JsonProcessingException {
    username = ChatHelper.constructWebSourceId(username);
    String botResponse = this.botService.handleTextContentAndReturnResponse(ModelNames.DEFAULT,
        request.getMessage(), username);

    return ResponseEntity.ok(botResponse);
  }

  @GetMapping(value = "/generate-image")
  public ResponseEntity<String> generateImage(@RequestParam String username)
      throws URISyntaxException, JsonProcessingException {
    username = ChatHelper.constructWebSourceId(username);
    List<String> keywords = this.textProcessingService.getKeywordsFromUsername(username);

    URI imageUri = this.botService.generateImageByKeywords(keywords);

    return ResponseEntity.ok(imageUri.toString());
  }
}
