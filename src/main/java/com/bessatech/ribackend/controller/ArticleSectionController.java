package com.bessatech.ribackend.controller;

import com.bessatech.ribackend.domain.dto.ArticleSectionSearchDTO;
import com.bessatech.ribackend.domain.dto.ArticleSectionSyncDTO;
import com.bessatech.ribackend.service.ArticleSectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/article-section")
public class ArticleSectionController {

    private final ArticleSectionService service;

    @PostMapping("/receive")
    public ResponseEntity<Void> importArticle(@RequestBody ArticleSectionSyncDTO section) {
        log.info("REST request sync crawler response of article: {}", section.getArticleId());
        service.syncArticleData(section);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/search")
    public Page<ArticleSectionSearchDTO> search(@RequestParam String term, Pageable pageable) {
        log.info("REST request to search by term: {}", term);
        return service.search(term, pageable);
    }
}
