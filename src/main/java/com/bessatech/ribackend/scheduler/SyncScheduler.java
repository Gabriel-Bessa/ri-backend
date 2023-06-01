package com.bessatech.ribackend.scheduler;

import com.bessatech.ribackend.domain.documents.elastic.Article;
import com.bessatech.ribackend.domain.documents.elastic.ArticleSection;
import com.bessatech.ribackend.domain.documents.mong.MArticle;
import com.bessatech.ribackend.domain.documents.mong.MArticleSection;
import com.bessatech.ribackend.repository.elastic.ArticleRepository;
import com.bessatech.ribackend.repository.elastic.ArticleSectionRepository;
import com.bessatech.ribackend.repository.mongo.MArticleRepository;
import com.bessatech.ribackend.repository.mongo.MArticleSectionRepository;
import com.bessatech.ribackend.service.feign.CrawlerFeign;
import com.bessatech.ribackend.service.mapper.ArticleMapper;
import com.bessatech.ribackend.service.mapper.ArticleSectionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SyncScheduler {

    private final MArticleRepository mArticleRepository;
    private final ArticleRepository articleRepository;
    private final MArticleSectionRepository mArticleSectionRepository;
    private final ArticleSectionRepository repository;
    private final ArticleSectionMapper mapper;
    private final ArticleMapper articleMapper;
    private final MongoOperations mongoOperations;
    private final CrawlerFeign crawlerFeign;

    @Transactional
    @Scheduled(fixedDelay = 500)
    public void scheduleSyncArticleSection() {
        PageRequest page = PageRequest.of(0, 1000);
        Page<MArticleSection> notSyncedData = mArticleSectionRepository.findAllByHasSync(false, page);
        if (!CollectionUtils.isEmpty(notSyncedData.getContent())) {
            log.debug("[SyncScheduler::scheduleSyncArticleSection] start to Sync Article Section Mongo with elastic");
            List<ArticleSection> elasticArticleSections = new ArrayList<>();
            notSyncedData.forEach(article -> {
                elasticArticleSections.add(mapper.toElasticEntity(article));
                article.setHasSync(true);
            });
            mArticleSectionRepository.saveAll(notSyncedData);
            repository.saveAll(elasticArticleSections);
            log.debug("[SyncScheduler::scheduleSyncArticleSection] end to Sync Article Section Mongo with elastic");
            return;
        }
        if (mArticleSectionRepository.count() != repository.count()) {
            cleanElasticAndMarkNotSyncMongo();
        }
    }

    @Transactional
    @Scheduled(fixedDelay = 500)
    public void scheduleSyncArticle() {
        PageRequest page = PageRequest.of(0, 1000);
        Page<MArticle> notSyncedData = mArticleRepository.findAllBySync(false, page);
        if (!CollectionUtils.isEmpty(notSyncedData.getContent())) {
            log.debug("[SyncScheduler::scheduleSyncArticle] start to Article Sync Mongo with elastic");
            List<Article> elasticArticleSections = new ArrayList<>();
            notSyncedData.forEach(article -> {
                Article art = articleMapper.toElasticEntity(article);
                elasticArticleSections.add(art);
                article.setSync(true);
            });
            articleRepository.saveAll(elasticArticleSections);
            mArticleRepository.saveAll(notSyncedData);
            log.debug("[SyncScheduler::scheduleSyncArticle] end to Article Sync Mongo with elastic");
            return;
        }
        if (mArticleSectionRepository.count() != repository.count()) {
            cleanElasticAndMarkNotSyncMongoArticle();
        }
    }

//    @Transactional
//    @Scheduled(fixedDelay = 500)
//    public void scheduleSyncArticle() {
//        PageRequest page = PageRequest.of(0, 1000);
//        if (articleRepository.count() > mArticleRepository.count()) {
//            log.debug("[SyncScheduler::scheduleSyncArticle] Start to Sync Elastic with Mongo");
//            Page<Article> notSyncedArticles = articleRepository.findAllBySync(false, page);
//            List<MArticle> entityToSync = new ArrayList<>();
//            notSyncedArticles.getContent().forEach(it -> {
//                MArticle mArticle = articleMapper.toMongoEntity(it);
//                mArticle.setSync(false);
//                it.setSync(true);
//                entityToSync.add(mArticle);
//            });
//            mArticleRepository.saveAll(entityToSync);
//            articleRepository.saveAll(notSyncedArticles);
//            log.debug("[SyncScheduler::scheduleSyncArticle] End to Sync Elastic with Mongo");
//        }
//    }

    @Scheduled(fixedDelay = 200000)
    public void startSecondyScrawler() {
        try {
            log.info("Call secondary crawler");
            crawlerFeign.startSecondyScrawler();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void cleanElasticAndMarkNotSyncMongo() {
        log.debug("[SyncScheduler::cleanElasticAndMarkNotSyncMongo] drop all data from elastic and sync mongo again");
        repository.deleteAll();
        Query query = new Query();
        Update update = new Update().set("hasSync", false);
        mongoOperations.updateMulti(query, update, MArticleSection.class);
    }

    private void cleanElasticAndMarkNotSyncMongoArticle() {
        log.debug("[SyncScheduler::cleanElasticAndMarkNotSyncMongoArticle] drop all data from elastic and sync mongo again");
        repository.deleteAll();
        Query query = new Query();
        Update update = new Update().set("sync", false);
        mongoOperations.updateMulti(query, update, MArticle.class);
    }
}
