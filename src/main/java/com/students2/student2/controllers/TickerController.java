package com.students2.student2.controllers;

import com.students2.student2.services.marketstack.MarketStackService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RestController
@Validated
@RequestMapping("/api")
public class TickerController {

    private final MarketStackService marketStackService;

    public TickerController(MarketStackService marketStackService) {
        this.marketStackService = marketStackService;
    }

    @PostMapping("/ticker/eod")
    public ResponseEntity<String> getTickerData(
            @Valid @NotEmpty @Size(min = 1, max = 10, message = "Number of symbols should be between 1 and 10")
            @RequestParam List<String> symbols) {

        int symbolCount = symbols.size();

        ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

        for (String symbol : symbols) {
            executorService.submit(() -> marketStackService.fetchAndSaveTickerData(symbolCount, symbol));
        }

        executorService.shutdown();

        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }

        return ResponseEntity.ok("Ticker data for symbols have been requested and will be fetched and saved.");
    }

}
