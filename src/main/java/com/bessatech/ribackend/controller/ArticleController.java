package com.bessatech.ribackend.controller;

import com.bessatech.ribackend.domain.dto.ArticleDTO;
import com.bessatech.ribackend.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/article")
public class ArticleController {

    private final ArticleService service;

    @PostMapping("/import")
    public ResponseEntity<Void> importArticle(@RequestPart("file") MultipartFile file) {
        log.debug("REST request to import CSV from crawler");
        service.importFile(file);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/not-crawled")
    public Page<ArticleDTO> getNotCrawled(Pageable pageable) {
        log.debug("REST request to find not crawled data");
        return service.getNotCrawledArticles(pageable);
    }

    @PostMapping("/filter")
    public Page<ArticleDTO> filterArticles(@RequestParam String term) {
        log.debug("REST request to filter articles");
        return service.filter(term);
    }

    @PostMapping("/clear")
    public ResponseEntity<Void> deleteArticle() {
        service.clearElastic();
        return ResponseEntity.ok().build();
    }
}
