package com.chat.line.service.api;

import java.util.List;

public interface TextProcessingService {

  List<String> getKeywordsFromUsername(String username);
}
