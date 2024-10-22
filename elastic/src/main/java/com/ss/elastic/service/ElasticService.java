package com.ss.elastic.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ElasticService {

    // 엘라스틱 서치로 데이터를 처리해야 됨.
    // 엘라스틱의 정보를 가지고 있는 엘라스틱 클라이언트 객체가 필요함.
    @Autowired
    private ElasticsearchClient elasticsearchClient;

    // 인덱스를 생성하는 메서드'
    public void createIndex(String indexName) throws IOException {
        System.out.println("createIndex()");
        System.out.println("Creating index " + indexName);

        // 인덱스를 생성하기 위한 요청 빌드하는 객체
        CreateIndexRequest request = new CreateIndexRequest.Builder()
                                        .index(indexName)   // 엘라스틱서치에서 인덱스 생성을 할 수 있도록 indexName 생성됨
                                        .build();

        // 인덱스 생성 요청 보내기
        // indices()
        // - 생성된 요청을 가지고 실제 엘라스틱 서치에서 인덱스를 생성하는 역할의 메서드
        CreateIndexResponse response = elasticsearchClient.indices().create(request);

        // 인덱스 생성 결과 확인
        if(response.acknowledged()) {
            System.out.println("인덱스 생성 성공!");
            System.out.println("Index " + indexName + " created");
        } else {
            System.out.println("인덱스 생성 실패!");
            System.out.println("Index " + indexName + " failed");
        };

    }
}
