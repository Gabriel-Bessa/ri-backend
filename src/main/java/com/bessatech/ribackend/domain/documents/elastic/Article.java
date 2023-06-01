package com.bessatech.ribackend.domain.documents.elastic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
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
@Document(indexName = "articles")
public class Article implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    @Field(type = FieldType.Text, name = "title")
    private String title;

    @Field(type = FieldType.Text, name = "link")
    private String link;

    @Field(type = FieldType.Text, name = "authors")
    private List<String> authors;

    @Field(type = FieldType.Text, name = "authorsLinks")
    private List<String> authorsLinks;

    @Field(type = FieldType.Boolean, name = "crawled")
    private Boolean crawled;

    @Field(type = FieldType.Boolean, name = "sync")
    private Boolean sync;

    @Transient
    private Float _score;
}
