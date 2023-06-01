package com.bessatech.ribackend.repository.elastic;

import com.bessatech.ribackend.domain.documents.elastic.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends ElasticsearchRepository<Article, String> {

    @Query("{\"bool\":{\"should\":[{\"match\":{\"title\":{\"query\":\":term\",\"fuzziness\":\"AUTO\"}}},{\"term\":{\"title\":\":term\"}}]}}")
    SearchHits<Article> searchByTitle(@Param("term") String term);

    @Query("{\"match\":{\"crawled\":false}}")
    Page<Article> getNotCrawledArticles(Pageable pageable);

    Page<Article> findAllBySync(boolean sync, Pageable pageable);
}
