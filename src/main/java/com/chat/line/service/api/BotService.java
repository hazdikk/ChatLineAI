package com.chat.line.service.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.linecorp.bot.client.base.BlobContent;
import com.linecorp.bot.webhook.model.MessageEvent;
import com.linecorp.bot.webhook.model.TextMessageContent;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.function.Consumer;

public interface BotService {

  void handleTextContent(String replyToken, MessageEvent event, TextMessageContent content)
      throws JsonProcessingException, URISyntaxException;

  String handleTextContentAndReturnResponse(String model, String message, String sourceId)
      throws JsonProcessingException;

  URI generateImageByKeywords(List<String> keywords) throws URISyntaxException, JsonProcessingException;

  URI generateImage(String content) throws URISyntaxException, JsonProcessingException;

  void handleHeavyContent(String replyToken, String messageId,
      Consumer<BlobContent> messageConsumer);
}
