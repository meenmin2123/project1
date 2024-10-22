package com.ss.elastic.controller;

import com.ss.elastic.service.ElasticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/elastic")
public class ElasticRestController {

    @Autowired
    private ElasticService service;

    // Http POST 요청
    @PostMapping("/create")
    public String createIndex(@RequestParam String indexName) {
        try {
            // 요청을 처리하는 메서드 클래스 생성
            // 인덱스 생성을 위해서 매개변수로 데이터를 넘김.
            service.createIndex(indexName);
            return "인덱스 생성 성공!" + indexName;
            
        } catch (Exception e) {
            e.printStackTrace();
            return "인덱스 생성 실패!" + indexName;
        }


    }


}
