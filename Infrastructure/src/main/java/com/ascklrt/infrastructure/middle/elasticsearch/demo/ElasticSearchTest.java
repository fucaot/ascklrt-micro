package com.ascklrt.infrastructure.middle.elasticsearch.demo;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.xcontent.XContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ElasticSearchTest {

    private RestHighLevelClient esClient;

    @Test
    void test() {
        System.out.println("1111111111122222222222");
    }

    @BeforeEach
    void init() {
        // 初始化
        esClient = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://127.0.0.1:9200")
        ));
        System.out.println(esClient);
    }

    @Test
    void createHotelIndex() throws IOException {
        // 2.1 创建索引库
        CreateIndexRequest create = new CreateIndexRequest("hotel");
        // 2.2 准备请求的参数，DSL语句
        create.source(DSLConstans.MAPPING_TEMPLATE, XContentType.JSON);
        // 2.3 发送请求
        esClient.indices().create(create, RequestOptions.DEFAULT);
    }

    @Test
    void deleteHotelIndex() throws IOException {
        // 2.1 创建索引库
        DeleteIndexRequest delete = new DeleteIndexRequest("hotel");
        // 2.3 发送请求
        esClient.indices().delete(delete, RequestOptions.DEFAULT);
    }

    @Test
    void existHotelIndex() throws IOException {
        GetIndexRequest getIndexRequest = new GetIndexRequest("hotel");
        boolean exists = esClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        System.out.println("index hotel is " + (exists ? "exist" : "not exist"));
    }

    @Test
    void putDockment() throws IOException {
        IndexRequest put = new IndexRequest("hotel").id("1");
        put.source("{\"name\": \"Jack\", \"age\" : \"21\"}", XContentType.JSON);
        esClient.index(put, RequestOptions.DEFAULT);
    }

    void close() throws IOException {
        // 销毁
        esClient.close();
    }
}
