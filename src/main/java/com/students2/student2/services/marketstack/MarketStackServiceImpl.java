package com.students2.student2.services.marketstack;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.students2.student2.entities.EndOfDay;
import com.students2.student2.repositories.MarketStackRepository;
import com.students2.student2.services.marketstack.dto.EndOfDayDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MarketStackServiceImpl implements MarketStackService {
    private static final Logger logger = LoggerFactory.getLogger(MarketStackServiceImpl.class);

    @Value("${student.app.MARKETSTACK_BASE_URL}")
    private String MARKETSTACK_BASE_URL;

    @Value("${student.app.MARKETSTACK_ACCESS_KEY}")
    private String MARKETSTACK_ACCESS_KEY;

    private final MarketStackRepository marketStackRepository;

    public MarketStackServiceImpl(MarketStackRepository marketStackRepository) {
        this.marketStackRepository = marketStackRepository;
    }

    public void fetchAndSaveTickerData(int symbolCount, String symbols) {
        try {
            int limit = 40;
            int offset = 0;
            int total;
            int retries = 3;

            do {
                String Url = MARKETSTACK_BASE_URL +
                        "?access_key=" + MARKETSTACK_ACCESS_KEY +
                        "&symbols=" + symbols +
                        "&limit=" + limit +
                        "&offset=" + offset;

                logger.info("Fetching data from URL: {}", Url);

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(Url))
                        .header("accept", "application/json")
                        .GET()
                        .build();

                HttpResponse<String> response;
                String responseBody = null;

                int retryCount = 0;
                boolean successfulResponse = false;

                while (retryCount < retries && !successfulResponse) {
                    response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    responseBody = response.body();

                    if (responseBody == null) {
                        logger.info("Received a null or non-OK response");
                        retryCount++;
                    } else {
                        successfulResponse = true;
                    }
                }

                if (!successfulResponse) {
                    logger.error("Failed to receive 'OK' response after {} retries", retries);
                }

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonDataNode = objectMapper.readTree(responseBody);

                JsonNode dataArray = jsonDataNode.get("data");

                if (dataArray == null || dataArray.isEmpty()) {
                    logger.info("No more data to fetch for symbol: " + symbols);
                    break;
                }

                total = jsonDataNode.get("pagination").get("total").asInt();

                List<EndOfDayDTO> endOfDayDTOList = extractEndOfDayDTOList(dataArray);

                saveEndOfDayData(endOfDayDTOList);

                offset += limit;

                try {
                    Thread.sleep(symbolCount * 50L);
                } catch (InterruptedException e) {
                    logger.error("Sleep interrupted: {}", e.getMessage());
                    Thread.currentThread().interrupt();
                }
            } while (total > offset);

        } catch (IOException | InterruptedException | ParseException e) {
            logger.error("Error occurred: {}", e.getMessage());
            e.printStackTrace();
        }
    }


    private List<EndOfDayDTO> extractEndOfDayDTOList(JsonNode dataArray) throws ParseException {
        List<EndOfDayDTO> endOfDayDTOList = new ArrayList<>();

        for (JsonNode data : dataArray) {
            String symbol = data.get("symbol").asText();
            String dateString = data.get("date").asText().substring(0, 10);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(dateString);
            float close = (float) data.get("close").asDouble();

            EndOfDayDTO endOfDayDTO = new EndOfDayDTO(symbol, date, close);

            endOfDayDTOList.add(endOfDayDTO);
        }

        if (!endOfDayDTOList.isEmpty()) {
            logger.info("Extracted {} EndOfDayDTO objects for symbol: {}", endOfDayDTOList.size(), endOfDayDTOList.get(0).getSymbol());
        } else {
            logger.info("No objects extracted for symbol: {}", endOfDayDTOList.get(0).getSymbol());
        }

        return endOfDayDTOList;
    }

    public void saveEndOfDayData(List<EndOfDayDTO> endOfDayDTOList) {
        ModelMapper modelMapper = new ModelMapper();
        List<EndOfDay> endOfDayEntities = endOfDayDTOList.stream()
                .map(endOfDayDTO -> modelMapper.map(endOfDayDTO, EndOfDay.class))
                .collect(Collectors.toList());

        if (!endOfDayEntities.isEmpty()) {
            logger.info("Saved {} EndOfDay entities for symbol: {}", endOfDayEntities.size(), endOfDayDTOList.get(0).getSymbol());
        } else {
            logger.info("No entities to save for symbol: {}", endOfDayDTOList.get(0).getSymbol());
        }

        marketStackRepository.saveAll(endOfDayEntities);
    }
}
