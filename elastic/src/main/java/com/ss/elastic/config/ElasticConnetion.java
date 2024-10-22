package com.ss.elastic.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class ElasticConnetion {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    // 엘라스틱 서치가 연결이 성공적으로 되었는지 확인 메서드

    // @PostConstruct
    // - 객체의 생성과 의존성주입이 완료된 후  실행해야 하는 초기화 메서드들을 정의할 때 사용한다.
    // 메서드가 실행하는 시점 spring 컨테이너에서 해당 객체의 생성자 호출 및 의존성 주입이 끝난 후 자동으로 실행된다.
    
    // * 제약 조건
    // - 매개변수가 없어야 함.
    // - 반환하는 타입이 void
    // - public
    
    // 먼저 엘라스틱 서치.bat 서버 실행 -> spring boot 실행
    @PostConstruct
    public void checkElasticConnection() {
        System.out.println("checkElasticConnection()");

        try {
            // ping()
            // - 서버가 동작 중인지, 즉 연결 할 수 있는 상태인지 확인하는 메서드

            // value()
            // - BooleanRespone객체로 반환 안에서 true, false 인지 값을 가져옴
            boolean isConnected = elasticsearchClient.ping().value();

            System.out.println("isConnected 실행 : " + isConnected);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
