package com.bessatech.ribackend.repository.elastic;

import com.bessatech.ribackend.domain.documents.elastic.ArticleSection;
import com.bessatech.ribackend.domain.documents.mong.MArticleSection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleSectionRepository extends ElasticsearchRepository<ArticleSection, String> {

    @Query("{\"bool\":{\"should\":[{\"match\":{\"paragraphs\":{\"query\":\":term\",\"operator\":\"OR\",\"fuzziness\":\"AUTO\"}}},{\"term\":{\"paragraphs\":\":term\"}}]}}")
    SearchPage<ArticleSection> searchByTerm(@Param("term") String term, Pageable pageable);

}
