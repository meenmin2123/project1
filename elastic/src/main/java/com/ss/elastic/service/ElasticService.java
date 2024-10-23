package com.ss.elastic.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.IndexOperation;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.List;

@Service
public class ElasticService {

    // 엘라스틱 서치로 데이터를 처리해야 됨.
    // 엘라스틱의 정보를 가지고 있는 엘라스틱 클라이언트 객체가 필요함.
    @Autowired
    private ElasticsearchClient elasticsearchClient;

    private final RestTemplate restTemplate;

    public ElasticService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

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

    // 한 개의 데이터만 저장하는 메서드(한 개 도큐먼트)
    public String InsertDocument(String indexName, String docId, Map<String, Object> docDataMap) throws IOException {
        System.out.println("ElasticService-InsertDocument()");

        // 문서를 엘라스틱에 추가하기 위해서 요청을 만듦.
        IndexRequest<Map<String, Object>> indexRequest = new IndexRequest.Builder<Map<String, Object>>()
                                                                         .index(indexName)   // 인덱스 이름 설정
                                                                         .index(docId)       // 문서 ID 설정
                                                                         .document(docDataMap)
                                                                         .build();
        // 엘라스틱 서치로 전송(restclient)하는 메서드
        IndexResponse resp = elasticsearchClient.index(indexRequest);

        // 결과 확인
        // - put 요청이 들어가게 되면 데이터가 없음 -> 데이터 생성, 데이터 있으면 -> 업데이트
        System.out.println("ElasticService-InsertDocument(): " + resp.toString());
        System.out.println(resp.result());
        return "문서가 성공적으로 추가되었음. ";
    }

    // 문서를 조회하는 메서드
    public Map<String, Object> getDocument(String indexName, String docId) {

        // 문서 조회 요청 객체 생성
        try {
            GetRequest request = new GetRequest.Builder().index(indexName).id(docId).build();

            // 엘라스틱에 전송
            // 첫번째 매개변수는 요청 객체 작성
            // 두번째 매개변수는 결과를 어떤 타입으로 받을지 설정
            GetResponse<Map> resp = elasticsearchClient.get(request, Map.class);

            if(resp.found()) {
                return resp.source();
            } else {
                System.out.println("ElasticService-GetDocument(): " + resp.toString());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 학생의 데이터를 한 번에 저장하는 메서드!
    public String bulkAddDocument(String indexName, List<Map<String, Object>> stu) throws Exception{

        // 1. 요청하는 객체를 빌더생성!
        BulkRequest.Builder bulkRequestBuilder = new BulkRequest.Builder();

        // 각 학생 데이터를 반복문으로 처리
        for(Map<String, Object> st : stu) {
            IndexOperation<Map<String, Object>> op = new IndexOperation
                                                        .Builder<Map<String, Object>>()
                                                        .index(indexName)  //인덱스 이름 설정
                                                        .document(st)      //학생 데이터 저장
                                                        .build();
            // 요청에 위에 문서를 추가
            BulkOperation bulkOperation = new BulkOperation.Builder().index(op).build();
            bulkRequestBuilder.operations(bulkOperation);
        }
        //전송
        // bulk() 메서드가 전송해서 처리한다.
        BulkRequest bulkRequest = bulkRequestBuilder.build();
        BulkResponse bulkResp  = elasticsearchClient
                .bulk(bulkRequest);

        System.out.println(bulkResp.toString());
        System.out.println(bulkResp.errors());
        // 2. 결과를 리턴하기!
        if(!bulkResp.errors()) {
            return "정상적으로 저장 되었습니다.";
        }else {
            return "저장 실패!!!";
        }

    }

    public String searchIndex() {
        String url = "http://localhost:9200/hee_index/_search?pretty";

        ResponseEntity<String> resp = restTemplate.getForEntity(url, String.class);

        return resp.getBody();
    }
}
