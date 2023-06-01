package com.bessatech.ribackend.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "crawler-feing", url = "${ri_final.crawler-url}")
public interface CrawlerFeign {

    @GetMapping("/run-script")
    void startSecondyScrawler();

}
