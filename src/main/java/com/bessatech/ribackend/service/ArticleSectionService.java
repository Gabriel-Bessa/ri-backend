package com.bessatech.ribackend.service;

import com.bessatech.ribackend.domain.documents.elastic.ArticleSection;
import com.bessatech.ribackend.domain.documents.mong.MArticleSection;
import com.bessatech.ribackend.domain.dto.ArticleSectionSearchDTO;
import com.bessatech.ribackend.domain.dto.ArticleSectionSyncDTO;
import com.bessatech.ribackend.repository.elastic.ArticleSectionRepository;
import com.bessatech.ribackend.repository.mongo.MArticleSectionRepository;
import com.bessatech.ribackend.service.mapper.ArticleSectionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleSectionService {

    private final ArticleSectionRepository repository;
    private final MArticleSectionRepository mRepository;
    private final ArticleSectionMapper mapper;
    private final ArticleService articleService;

    @Transactional
    public void syncArticleData(ArticleSectionSyncDTO article) {
        final String articleId = article.getArticleId();
        List<MArticleSection> articleSections = article.getSections().stream().map(mapper::toMongoEntity).toList();
        if (articleService.existsByArticleId(articleId)) {
            articleSections.forEach(it -> it.setArticleId(articleId));
            mRepository.saveAll(articleSections);
            articleService.updateArticleCrawled(articleId);
        }
    }

    public Page<ArticleSectionSearchDTO> search(String term, Pageable pageable) {
        SearchPage<ArticleSection> searchHits = repository.searchByTerm(term, pageable);
        return searchHits.map(section -> {
            ArticleSectionSearchDTO articleSection = mapper.toDto(section.getContent());
            articleSection.setScore(section.getScore());
            return articleSection;
        });
    }
}
