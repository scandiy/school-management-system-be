package com.students2.student2.services.marketstack;

import com.students2.student2.services.marketstack.dto.EndOfDayDTO;

import java.util.List;

public interface MarketStackService {

    void fetchAndSaveTickerData(int symbolCount, String symbols);

    void saveEndOfDayData(List<EndOfDayDTO> endOfDayDTOList);
}
