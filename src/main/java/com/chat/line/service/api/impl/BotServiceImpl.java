package com.chat.line.service.api.impl;

import com.chat.line.model.constant.ModelNames;
import com.chat.line.model.constant.RoleNames;
import com.chat.line.model.entity.ChatMessage;
import com.chat.line.model.entity.ChatRequest;
import com.chat.line.model.entity.ImageData;
import com.chat.line.model.entity.ImageRequest;
import com.chat.line.model.entity.ImageResponse;
import com.chat.line.model.enums.PromptType;
import com.chat.line.service.api.BotService;
import com.chat.line.service.api.ChatGptService;
import com.chat.line.service.api.TextProcessingService;
import com.chat.line.service.helper.ChatGptHelper;
import com.chat.line.service.helper.ChatHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.linecorp.bot.client.base.BlobContent;
import com.linecorp.bot.client.base.Result;
import com.linecorp.bot.messaging.client.MessagingApiClient;
import com.linecorp.bot.messaging.model.ImageMessage;
import com.linecorp.bot.messaging.model.Message;
import com.linecorp.bot.messaging.model.ReplyMessageRequest;
import com.linecorp.bot.messaging.model.ReplyMessageResponse;
import com.linecorp.bot.messaging.model.TextMessage;
import com.linecorp.bot.webhook.model.MessageEvent;
import com.linecorp.bot.webhook.model.TextMessageContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static java.util.Collections.singletonList;

@Slf4j
@Service
@RequiredArgsConstructor
public class BotServiceImpl implements BotService {

  private final Map<String, List<String>> conversationsByName;
  private final MessagingApiClient messagingApiClient;
  private final ChatGptService chatGptService;
  private final TextProcessingService textProcessingService;

  @Value("${gpt.generate-image-prompt}")
  private String generateImagePromptPrefix;

  @Value("${gpt.system-content-prompt-default}")
  private String systemContentPromptDefault;

  @Override
  public void handleTextContent(String replyToken, MessageEvent event, TextMessageContent content)
      throws JsonProcessingException, URISyntaxException {
    PromptType promptType = PromptType.getPromptTypeOrDefault(content.text());
    String prompt = ChatHelper.getPrompt(promptType, content.text());
    String sourceId = ChatHelper.constructSourceId(event);

    this.handleTextContent(replyToken, promptType, prompt, sourceId);
  }

  private void handleTextContent(String replyToken, PromptType promptType, String prompt,
      String sourceId) throws JsonProcessingException, URISyntaxException {
    if (PromptType.GPT_4.equals(promptType)) {
      this.handleTextContentAndReplyText(replyToken, ModelNames.GPT_4, prompt, sourceId);
    } else if (PromptType.CREATE_IMAGE.equals(promptType)) {
      this.handleTextContentAndReplyImage(replyToken, prompt);
    } else if (PromptType.KEYWORD.equals(promptType)) {
      List<String> keywords = this.textProcessingService.getKeywordsFromUsername(sourceId);
      this.reply(replyToken, new TextMessage(String.join(",", keywords)));
    } else if (PromptType.ADS.equals(promptType)) {
      List<String> keywords = this.textProcessingService.getKeywordsFromUsername(sourceId);
      this.replyImageFromKeywords(replyToken, keywords);
    } else {
      this.handleTextContentAndReplyText(replyToken, ModelNames.DEFAULT, prompt, sourceId);
    }
  }

  private void handleTextContentAndReplyText(String replyToken, String model, String message,
      String sourceId) throws JsonProcessingException {
    this.reply(replyToken,
        new TextMessage(this.handleTextContentAndReturnResponse(model, message, sourceId)));
  }
  
  @Override
  public String handleTextContentAndReturnResponse(String model, String message, String sourceId)
      throws JsonProcessingException {
    List<String> messages = ChatHelper.getUserMessages(conversationsByName, sourceId, message);
    List<ChatMessage> userMessages = ChatGptHelper.constructUserMessages(messages);

    return this.chatGptService.chat(
        ChatGptHelper.constructCompletionsRequest(model, userMessages, systemContentPromptDefault))
        .getContent();
  }
  
  private void replyImageFromKeywords(String replyToken, List<String> keywords)
      throws URISyntaxException, JsonProcessingException {
    URI generatedImageUri = this.generateImageByKeywords(keywords);

    this.reply(replyToken, new ImageMessage(generatedImageUri, generatedImageUri));
  }

  private void handleTextContentAndReplyImage(String replyToken, String content)
      throws JsonProcessingException, URISyntaxException {
    URI uri = this.generateImage(content);

    this.reply(replyToken, new ImageMessage(uri, uri));
  }

  @Override
  public URI generateImageByKeywords(List<String> keywords)
      throws URISyntaxException, JsonProcessingException {
    String generateImagePrompt =
        ChatGptHelper.constructGenerateImagePromptWithKeywords(keywords, generateImagePromptPrefix);
    return this.generateImage(generateImagePrompt);
  }

  @Override
  public URI generateImage(String content) throws URISyntaxException, JsonProcessingException {
    ImageRequest imageRequest = ChatGptHelper.constructImageRequest(content);
    ImageData imageData = this.chatGptService.generateImage(imageRequest);

    return new URI(imageData.getUrl());
  }

  @Override
  public void handleHeavyContent(String replyToken, String messageId,
      Consumer<BlobContent> messageConsumer) {

  }

  private void reply(String replyToken, Message message) {
    Objects.requireNonNull(replyToken, "replyToken");
    Objects.requireNonNull(message, "message");
    reply(replyToken, singletonList(message));
  }

  private void reply(String replyToken, List<Message> messages) {
    Objects.requireNonNull(replyToken, "replyToken");
    Objects.requireNonNull(messages, "messages");
    reply(replyToken, messages, false);
  }

  private void reply(String replyToken, List<Message> messages, boolean notificationDisabled) {
    try {
      Objects.requireNonNull(replyToken, "replyToken");
      Objects.requireNonNull(messages, "messages");
      Result<ReplyMessageResponse> apiResponse = messagingApiClient
          .replyMessage(new ReplyMessageRequest(replyToken, messages, notificationDisabled)).get();
      log.info("Sent messages: {}", apiResponse);
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
  }
}
