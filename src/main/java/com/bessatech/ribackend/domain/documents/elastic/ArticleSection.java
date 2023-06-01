package com.bessatech.ribackend.domain.documents.elastic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "articles_section")
public class ArticleSection implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    @Field(type = FieldType.Text, name = "article_id")
    private String articleId;

    @Field(type = FieldType.Text, name = "section")
    private String section;

    @Field(type = FieldType.Text, name = "paragraphs")
    private List<String> paragraphs;

}
