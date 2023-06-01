package com.bessatech.ribackend.repository.mongo;

import com.bessatech.ribackend.domain.documents.mong.MArticleSection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MArticleSectionRepository extends MongoRepository<MArticleSection, String> {

    Page<MArticleSection> findAllByHasSync(boolean synced, Pageable pageable);

}
