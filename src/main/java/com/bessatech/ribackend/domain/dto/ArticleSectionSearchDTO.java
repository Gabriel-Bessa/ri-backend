package com.bessatech.ribackend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleSectionSearchDTO extends ElasticScore implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String section;
    private String articleId;
    private List<String> paragraphs;
}
