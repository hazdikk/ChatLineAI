package com.chat.line.service.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.chat.line.service.api.KeywordExtractionService;
import com.chat.line.service.api.TextProcessingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TextProcessingServiceImpl implements TextProcessingService {

  private final KeywordExtractionService keywordExtractionService;
  private final Map<String, List<String>> conversationsByName;

  @Override
  public List<String> getKeywordsFromUsername(String username) {
    List<String> recentConversations = conversationsByName.get(username);
    return this.getKeywordsFromConversations(recentConversations);
  }

  private List<String> getKeywordsFromConversations(List<String> conversations) {
    List<String> keywords = new ArrayList<>();

    for (String conversation : conversations) {
      keywords.addAll(this.keywordExtractionService.extractKeywords(conversation));
    }

    return keywords;
  }
}
