package com.bessatech.ribackend.domain.documents.mong;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("articles_section")
public class MArticleSection implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    private String articleId;

    private String section;

    private List<String> paragraphs;
    private boolean hasSync = false;

}
