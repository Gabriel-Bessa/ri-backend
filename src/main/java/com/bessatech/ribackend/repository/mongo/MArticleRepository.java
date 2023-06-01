package com.bessatech.ribackend.repository.mongo;

import com.bessatech.ribackend.domain.documents.mong.MArticle;
import com.bessatech.ribackend.domain.documents.mong.MArticleSection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MArticleRepository extends MongoRepository<MArticle, String> {
    Page<MArticle> findAllBySync(boolean synced, Pageable pageable);


}
