package com.chat.line.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageRequest implements Serializable {
  @Serial
  private static final long serialVersionUID = -3922230846196497597L;

  private String model;
  private String prompt;
  private String size;
  private String quality;
}
