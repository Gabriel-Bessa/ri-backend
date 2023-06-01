package com.bessatech.ribackend.service;

import com.bessatech.ribackend.domain.documents.elastic.Article;
import com.bessatech.ribackend.domain.dto.ArticleDTO;
import com.bessatech.ribackend.repository.elastic.ArticleRepository;
import com.bessatech.ribackend.service.mapper.ArticleMapper;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterables;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    private static final String DEMILITER = "#";
    private final ArticleRepository repository;
    private final ArticleMapper mapper;

    @Transactional
    public void importFile(MultipartFile file) {
        List<Article> articles = new ArrayList<>();
        long lines = 0;
        try {
            String line = "";
            BufferedReader csvReader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            csvReader.readLine();
            while ((line = csvReader.readLine()) != null) {
                lines++;
                try {
                    processLine(line, articles);
                } catch (Exception e) {
                    String[] splitedLine = line.split(DEMILITER);
                    log.error(splitedLine[0]);
                }
            }
        } catch (IOException e) {
            log.error("Found an error in import of discounts: " + e.getMessage());
        }
        Iterables.partition(articles, 50000).iterator().forEachRemaining(repository::saveAll);
    }

    private void processLine(String line, List<Article> articles) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        Article article = new Article();
        String[] splitedLine = line.split(DEMILITER);
        article.setId(splitedLine[0]);
        article.setTitle(mapper.readValue(splitedLine[1], String[].class)[0]);
        article.setLink(splitedLine[2]);
        article.setAuthors(mapper.readValue(splitedLine[3], List.class));
        article.setAuthorsLinks(mapper.readValue(splitedLine[4], List.class));
        article.setCrawled(Boolean.FALSE);
        article.setSync(Boolean.FALSE);
        articles.add(article);
    }

    @Transactional
    public void clearElastic() {
        repository.deleteAll();
    }

    public Page<ArticleDTO> getNotCrawledArticles(Pageable pageable) {
        return repository.getNotCrawledArticles(pageable).map(it -> {
            ArticleDTO article = mapper.toDto(it);
            article.setLink(article.getLink().replace("http://", "https://"));
            return article;
        });
    }

    public Page<ArticleDTO> filter(String term) {
        SearchHits<Article> searchHits = repository.searchByTitle(term);
        return new PageImpl<>(searchHits.stream().map(it -> {
            ArticleDTO dto = mapper.toDto(it.getContent());
            dto.setScore(it.getScore());
            return dto;
        }).toList());
    }

    public boolean existsByArticleId(String articleId) {
        return repository.existsById(articleId);
    }

    @Transactional
    public void updateArticleCrawled(String articleId) {
        repository.findById(articleId).ifPresent(article -> {
            article.setCrawled(Boolean.TRUE);
            repository.save(article);
        });
    }
}
