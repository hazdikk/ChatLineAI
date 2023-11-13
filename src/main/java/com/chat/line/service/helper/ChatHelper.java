package com.chat.line.service.helper;

import com.chat.line.model.enums.PromptType;
import com.linecorp.bot.webhook.model.GroupSource;
import com.linecorp.bot.webhook.model.MessageEvent;
import com.linecorp.bot.webhook.model.RoomSource;
import com.linecorp.bot.webhook.model.UserSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChatHelper {

  private static final String SPACE_DELIMITER = " ";
  private static final String DASH_DELIMITER = "-";
  private static final int MAX_SIZE = 10;
  private static final String PRIVATE_CHAT_PREFIX = "PRIVATE";
  private static final String WEBSITE_CHAT_PREFIX = "WEB";

  public static String getPromptType(String input) {
    int spaceIndex = input.indexOf(SPACE_DELIMITER);
    return input.substring(0, spaceIndex);
  }

  public static String getPrompt(PromptType promptType, String content) {
    return content.substring(promptType.getValue().length()).trim();
  }

  public static List<String> getUserMessages(Map<String, List<String>> conversationsByName,
      String name, String newMessage) {
    List<String> lastUserMessages =
        conversationsByName.computeIfAbsent(name, k -> new ArrayList<>());

    if (lastUserMessages.size() == MAX_SIZE) {
      lastUserMessages.remove(0);
    }
    lastUserMessages.add(newMessage);

    return lastUserMessages;
  }

  public static String constructSourceId(MessageEvent event) {
    if (event.source() instanceof GroupSource groupSource) {
      return groupSource.groupId() + DASH_DELIMITER + groupSource.userId();
    } else if (event.source() instanceof RoomSource roomSource) {
      return roomSource.roomId() + DASH_DELIMITER + roomSource.userId();
    } else if (event.source() instanceof UserSource userSource) {
      return PRIVATE_CHAT_PREFIX + DASH_DELIMITER + userSource.userId();
    } else {
      return null;
    }
  }

  public static String constructWebSourceId(String username) {
    return WEBSITE_CHAT_PREFIX + DASH_DELIMITER + username;
  }
}
