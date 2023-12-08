package com.chat.line.service.api;

import java.util.List;
import java.util.Set;

public interface TextProcessingService {

  Set<String> getKeywordsFromUsername(String username);
}
