package com.bessatech.ribackend.service.mapper;

import com.bessatech.ribackend.domain.documents.elastic.Article;
import com.bessatech.ribackend.domain.documents.mong.MArticle;
import com.bessatech.ribackend.domain.dto.ArticleDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleMapper {

    ArticleDTO toDto(Article entity);

    MArticle toMongoEntity(Article entity);
    Article toElasticEntity(MArticle entity);

}
