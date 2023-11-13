package com.chat.line.service.api;

import java.util.List;

public interface KeywordExtractionService {

  List<String> extractKeywords(String text);
}
