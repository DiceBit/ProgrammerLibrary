package com.example.webapp3.Service;

import com.example.webapp3.Models.Book;
import com.example.webapp3.Repositories.BookRepository;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.client.erhlc.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Service
public class ESService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    public void ESword(){

        System.out.println("ESService Bean started...");

        long count = 0;

        try {

            System.out.println("Create credentialsProvider");
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("elastic","26y2xE*no8sGLFpt6xDJ"));

            System.out.println("Connection...");
            RestHighLevelClient restHighLevelClient = new RestHighLevelClient(
                    RestClient.builder(new HttpHost(
                            "localhost",
                            9200,
                            "http"))
                            .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                                @Override
                                public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpAsyncClientBuilder) {
                                    return httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                                }
                            }));
            System.out.println("Connection to DB...");
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Create index request...");
            CreateIndexRequest request = new CreateIndexRequest("es_books");
            request.settings(Settings
                    .builder()
                    .put("index.number_of_shards", 1)
                    .put("index.number_of_replicas", 2)
            );

            request.mapping(
                    "{\n" +
                            "  \"properties\": {\n" +
                            "    \"bookName\": {\n" +
                            "      \"type\": \"text\"\n" +
                            "    },\n" +
                            "    \"tag\": {\n" +
                            "      \"type\": \"keyword\"\n" +
                            "    },\n" +
                            "    \"description\": {\n" +
                            "      \"type\": \"text\"\n" +
                            "    },\n" +
                            "    \"price\": {\n" +
                            "      \"type\": \"integer\"\n" +
                            "    },\n" +
                            "    \"Img\": {\n" +
                            "      \"type\": \"text\"\n" +
                            "    },\n" +
                            "    \"fileName\": {\n" +
                            "      \"type\": \"text\"\n" +
                            "    },\n" +
                            "    \"date\": {\n" +
                            "      \"type\": \"date\"\n" +
                            "    },\n" +
                            "    \"reviews\": {\n" +
                            "      \"type\": \"nested\",\n" +
                            "      \"properties\": {\n" +
                            "        \"id\": {\n" +
                            "          \"type\": \"long\"\n" +
                            "        },\n" +
                            "        \"text\": {\n" +
                            "          \"type\": \"text\"\n" +
                            "        },\n" +
                            "        \"rating\": {\n" +
                            "          \"type\": \"integer\"\n" +
                            "        }\n" +
                            "      }\n" +
                            "    }\n" +
                            "  }\n" +
                            "}",
                    XContentType.JSON
            );
            System.out.println("create index response...");
            //CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from book;");
            System.out.println("add to index...");
            while (resultSet.next()){
                IndexRequest indexRequest = new IndexRequest("es_books");
                indexRequest.id(resultSet.getString("id"));
                Map<String, Object> jsonMap = new HashMap<>();
                jsonMap.put("book_name", resultSet.getString("book_name"));
                jsonMap.put("tag", resultSet.getString("tag"));
                jsonMap.put("description", resultSet.getString("description"));
                jsonMap.put("price", resultSet.getInt("price"));
                jsonMap.put("Img", resultSet.getString("Img"));
                jsonMap.put("file_name", resultSet.getString("file_name"));
                //jsonMap.put("date", resultSet.getDate("date"));

                    indexRequest.source(jsonMap);
                IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            }

            System.out.println("counted...");
            CountRequest countRequest = new CountRequest("es_books");
            CountResponse countResponse = restHighLevelClient.count(countRequest, RequestOptions.DEFAULT);

            count = countResponse.getCount();

            restHighLevelClient.close();
            connection.close();

        } catch (Exception e){
            System.out.println("exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("Кол-во записей в таблице: " + count);
            System.out.println("ESService Bean stopped...");
        }
    }

    public void indexEntity(){
        List<Book> entities = bookRepository.findAll();

        List<IndexQuery> queries = entities.stream()
                .map(entity -> new IndexQueryBuilder()
                        .withId(entity.getId().toString())
                        .withObject(entity)
                        .build())
                .collect(Collectors.toList());

        elasticsearchOperations.save(queries);
        //elasticsearchOperations.indexOps(Book.class).create();
    }
    public void updateIndex(Long id){
        Book book = bookRepository.findById(id).orElseThrow();

        IndexQuery query = new IndexQueryBuilder()
                .withId(id.toString())
                .withObject(book)
                .build();

        elasticsearchOperations.save(query);
        //elasticsearchOperations.indexOps(Book.class).create();
    }
    public List<Book> searchByName(String name) {
        /*Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("name", name))
                .build();


        SearchHits<Book> bookSearchHits =   elasticsearchOperations.search(name, Book.class, IndexCoordinates.of("b"))
 */
        return null;
    }
    public Book findById(Long id){
        Book book = elasticsearchOperations.get(id.toString(), Book.class);
        return book;
    }
}
