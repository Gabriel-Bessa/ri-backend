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
public class ArticleDTO extends ElasticScore implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    private String title;

    private String link;

    private List<String> authors;

    private List<String> authorsLinks;

    private Boolean crawled;
}
