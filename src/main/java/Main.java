import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static ObjectMapper mapper = new ObjectMapper();

    public static List<Cat> filterCats(List<Cat> cats, Integer upvotes){

        Stream<Cat> cat = cats.stream()
                .filter(upvotesFiltred -> upvotesFiltred.getUpvotes() != upvotes && upvotesFiltred.getUpvotes() > 0);

        return cat.collect(Collectors.toList());
    }

    public static void main(String[] args) throws IOException {


        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5_000)        // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30_000)        // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false)      // возможность следовать редиректу в ответе
                        .build())
                .build();

        HttpGet request =
                new HttpGet("https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats");

        CloseableHttpResponse response = httpClient.execute(request);

//        Arrays.stream(response.getAllHeaders()).forEach(System.out::println);

        List<Cat> cats = mapper.readValue(response.getEntity().getContent(), new TypeReference<List<Cat>>() {});

        cats.forEach(System.out::println);

        System.out.println();
        System.out.println("Отфильтруйте список - например, средствами stream api, с помощью метода filter(value -> value.getUpvotes() != null && value.getUpvotes() > 0);");
        System.out.println();

        cats = filterCats(cats, null);

        cats.forEach(System.out::println);


    }
}
