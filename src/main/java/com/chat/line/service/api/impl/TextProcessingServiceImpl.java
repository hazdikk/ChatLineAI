package com.chat.line.service.api.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
  public Set<String> getKeywordsFromUsername(String username) {
    List<String> recentConversations = conversationsByName.get(username);
    return this.getKeywordsFromConversations(recentConversations);
  }

  private Set<String> getKeywordsFromConversations(List<String> conversations) {
    Set<String> keywords = new HashSet<>();

    for (String conversation : conversations) {
      keywords.addAll(this.keywordExtractionService.extractKeywords(conversation));
    }

    return keywords;
  }
}
