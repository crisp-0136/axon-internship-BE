package ro.axon.dot.service;



import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.axon.dot.domain.LegallyDaysOffEty;
import ro.axon.dot.domain.repositories.LegallyDaysOffRepository;
import ro.axon.dot.model.LegallyDaysOffDto;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class LegallyDaysOffService {

    private final LegallyDaysOffRepository legallyDaysOffRepository;

    public LegallyDaysOffService(LegallyDaysOffRepository legallyDaysOffRepository) {
        this.legallyDaysOffRepository = legallyDaysOffRepository;
    }


    @Transactional(readOnly = true)
    @Cacheable(value = "daysOffCache", key = "{#periods, #years}")
    public List<LegallyDaysOffDto> findLegallyDaysOff(List<String> periods, List<String> years) {

        List<LegallyDaysOffEty> entities;

        // If both periods and years are missing, return all data
        if ((periods == null || periods.isEmpty()) && (years == null || years.isEmpty())) {
            entities = legallyDaysOffRepository.findAll();
        }
        // If both periods and years are provided, filter by years (ignore periods)
        else if (years != null && !years.isEmpty()) {
            entities = legallyDaysOffRepository.findByYearIn(years);
        }
        // If only periods are provided, filter by periods
        else {
            entities = legallyDaysOffRepository.findByPeriodIn(periods);
        }

        // Convert entities to DTOs
        return entities.stream()
                .map(entity -> new LegallyDaysOffDto(
                        entity.getDate(),
                        entity.getDescription()
                ))
                .collect(Collectors.toList());
    }
}
