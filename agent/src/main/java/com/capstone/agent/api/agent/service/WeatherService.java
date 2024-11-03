package com.capstone.agent.api.agent.service;
import com.capstone.agent.api.agent.dto.WeatherDTO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import reactor.core.publisher.Mono;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {
    @Value("${weather.api.decodingKey}")
    private String serviceKey;

    private final WebClient webClient;

    public WeatherDTO getWeatherForecast() {
        
        LocalDateTime time = LocalDateTime.now().minusMinutes(30); // 현재 시각 30분 전
        String baseDate = time.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String baseTime = time.format(DateTimeFormatter.ofPattern("HHmm"));
        
        // 충주 좌표
        String nx = "76";             // X 좌표값
        String ny = "114";            // Y 좌표값

        String url = "/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst";
        Mono<String> response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("apis.data.go.kr")
                        .path(url)
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("pageNo", "1")
                        .queryParam("numOfRows", "1000")
                        .queryParam("dataType", "XML")
                        .queryParam("base_date", baseDate)
                        .queryParam("base_time", baseTime)
                        .queryParam("nx", nx)
                        .queryParam("ny", ny)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
        // 비동기 응답 처리 (block() 사용 시 동기 처리)
        String xmlResponse = response.block();
        return parseWeatherData(xmlResponse);
    }

    private WeatherDTO parseWeatherData(String xmlData) {
        WeatherDTO weather = WeatherDTO.builder()
                .weathers(new HashMap<>())
                .build();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xmlData)));

            NodeList itemList = document.getElementsByTagName("item");

            for (int i = 0; i < itemList.getLength(); i++) {
                Element itemElement = (Element) itemList.item((i));

                String category = itemElement.getElementsByTagName("category").item(0).getTextContent();
                String fcstTime = itemElement.getElementsByTagName("fcstTime").item(0).getTextContent();
                String fcstValue = itemElement.getElementsByTagName("fcstValue").item(0).getTextContent();
                
                
                weather.getWeathers().putIfAbsent(fcstTime, new HashMap<>());
                
                //if (category.equals("T1H")) {
                if (category.equals("TMP")) {
                    weather.getWeathers().get(fcstTime).put("temper", fcstValue);
                } else if (category.equals("SKY")) {
                    weather.getWeathers().get(fcstTime).put("sky", skyCode(fcstValue));
                }
            }
        } catch (Exception e) {
            log.error("데이터 변환 에러", e);
        }
        return weather;
    }

    private String skyCode(String code) {
        int skyCode = Integer.parseInt(code);
        if (skyCode <= 5) {
            return "맑음";
        } else if (skyCode <= 8) {
            return "구름 많음";
        } else {
            return "흐림";
        }
    }
}