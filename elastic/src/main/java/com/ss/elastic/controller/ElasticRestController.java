package com.ss.elastic.controller;

import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import org.springframework.web.client.RestTemplate;
import com.ss.elastic.service.ElasticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestOperations;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

    // http://localhost:8080/elastic/create?indexName=my_index
    // my_index에 도큐먼트 저장하는 메서드
    // ex) PUT my_index/_doc/1
    @PostMapping("/add/{indexName}/{docId}")
    public String InsertDocument(@PathVariable String indexName, @PathVariable String docId) throws IOException {
        System.out.println("InsertDocument() 실행");
        System.out.println("docId : " + docId);

        // 문서 데이터 랜덤 생성
        Map<String, Object> docDataMap = randomDocument();

        try {
            return service.InsertDocument(indexName, docId, docDataMap);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @GetMapping("/get/{indexName}/{docId}")
    public Map<String, Object> getDocument(@PathVariable String indexName, @PathVariable String docId) {
        System.out.println("getDocument()");

        Map<String, Object> docMap = service.getDocument(indexName, docId);
        System.out.println(docMap.toString());

        return docMap;
    }

    // 학생의 데이터를 한 번에 저장하는 메서드!
    public String bulkAddDocument(String indexName, List<Map<String, Object>> stu) {

        return "";
    }




    private Map<String, Object> randomDocument() {

        Random randomId = new Random();
        Map<String, Object> docData = new HashMap<String, Object>();

        docData.put("name", "user" + randomId.nextInt(100));
        docData.put("age", randomId.nextInt(40) + 20);
        docData.put("gender", randomId.nextBoolean() ? "male" : "female");

        System.out.println("docData : " + docData);

        return docData;
    }

}
