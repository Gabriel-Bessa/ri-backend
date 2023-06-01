package com.bessatech.ribackend.service.mapper;

import com.bessatech.ribackend.domain.documents.elastic.ArticleSection;
import com.bessatech.ribackend.domain.documents.mong.MArticleSection;
import com.bessatech.ribackend.domain.dto.ArticleSectionDTO;
import com.bessatech.ribackend.domain.dto.ArticleSectionSearchDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ArticleSectionMapper {

    MArticleSection toMongoEntity(ArticleSectionDTO dto);

    @Mapping(target = "articleId", source = "id")
    ArticleSectionSearchDTO toDto(ArticleSection entity);

    @Mapping(target = "articleId", source = "id")
    ArticleSection toElasticEntity(MArticleSection entity);
}
