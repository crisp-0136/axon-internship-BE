package ro.axon.dot.service;


import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ro.axon.dot.domain.LegallyDaysOffEty;
import ro.axon.dot.domain.repositories.LegallyDaysOffRepository;

import java.util.List;

@Service
public class LegallyDaysOffService {

    private final LegallyDaysOffRepository legallyDaysOffRepository;

    public LegallyDaysOffService(LegallyDaysOffRepository legallyDaysOffRepository) {
        this.legallyDaysOffRepository = legallyDaysOffRepository;
    }

    @Cacheable(value = "daysOffCache", key = "{#periods, #years}")
    public List<LegallyDaysOffEty> findLegallyDaysOff(List<String> periods, List<String> years) {

        // If both periods and years are missing, return all data
        if ((periods == null || periods.isEmpty()) && (years == null || years.isEmpty())) {
            return legallyDaysOffRepository.findAll();
        }

        // If both periods and years are provided, filter by years (ignore periods)
        if (years != null && !years.isEmpty()) {
            return legallyDaysOffRepository.findByYearIn(years);
        }

        // If only periods are provided, filter by periods
        return legallyDaysOffRepository.findByPeriodIn(periods);
    }
}
