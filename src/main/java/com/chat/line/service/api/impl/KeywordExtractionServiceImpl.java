package com.chat.line.service.api.impl;

import com.chat.line.service.api.KeywordExtractionService;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class KeywordExtractionServiceImpl implements KeywordExtractionService {

  private final Tokenizer tokenizer;
  private final POSTaggerME posTagger;

  public KeywordExtractionServiceImpl() throws Exception {
    // Load tokenization model
    try (InputStream modelIn = getClass().getResourceAsStream("/models/en-token.bin")) {
      tokenizer = new TokenizerME(new TokenizerModel(modelIn));
    }

    // Load POS tagging model
    try (InputStream modelIn = getClass().getResourceAsStream("/models/en-pos-maxent.bin")) {
      posTagger = new POSTaggerME(new POSModel(modelIn));
    }
  }

  @Override
  public List<String> extractKeywords(String text) {
    List<String> keywords = new ArrayList<>();

    // Tokenize the text
    String[] tokens = tokenizer.tokenize(text);

    // Tag the tokens with parts of speech
    String[] tags = posTagger.tag(tokens);

    // Extract keywords based on POS tags (e.g., nouns)
    for (int i = 0; i < tokens.length; i++) {
      if (tags[i].startsWith("NN")) { // NN represents noun
        keywords.add(tokens[i]);
      }
    }

    return keywords;
  }

}
