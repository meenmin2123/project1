package com.ss.elastic.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 서버가 실행할 때 설정하는 객체 타입
@Configuration
public class ElasticConfing {

    // 1. 클라이언트와 서버 간의 데이터를 주고 받는 통로
    // RestClient 와 json 매퍼를 결합해 엘라스틱서치와 통신을 담당하는 클래스
    // -> RestClientTransport

    @Bean
    public ElasticsearchClient elasticsearchClients() {
        System.out.println("elasticsearchClients()");

        RestClient restClient = RestClient.builder(new HttpHost("localhost", 9200, "http")).build();

        // JacksonJsonpMapper 객체를 생성하는 이유는 json 타입의 데이터를 java 객체로 변환하는 매퍼
        // java인 객체를 json 타입으로 자동으로 변환해주는 라이브러리
        RestClientTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

        return new ElasticsearchClient(transport);
    }

    // 2. Http 요청을 보낼 호스트 정보를 담는 객체
    //    서버의 주소 localhost, port 번호:9200 -> HttpHost

    // 3. 서버에서 Http요청을 내보내는 도구 -> RestClient

}

