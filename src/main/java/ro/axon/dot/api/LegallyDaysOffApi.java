package ro.axon.dot.api;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ro.axon.dot.model.LegallyDaysOffDto;
import ro.axon.dot.service.LegallyDaysOffService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/misc")
public class LegallyDaysOffApi {

    private final LegallyDaysOffService legallyDaysOffService;

    public LegallyDaysOffApi(LegallyDaysOffService legallyDaysOffService) {
        this.legallyDaysOffService = legallyDaysOffService;
    }

    @GetMapping("/legally-days-off")
    public List<LegallyDaysOffDto> findLegallyDaysOff(
            @RequestParam(required = false) List<String> periods,
            @RequestParam(required = false) List<String> years) {

        return legallyDaysOffService.findLegallyDaysOff(periods, years);
    }
}