package ro.axon.dot.config;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.axon.dot.mapper.TeamMapper;

@Configuration
public class MapperConfig {

    @Bean
    public TeamMapper teamMapper() {
        return Mappers.getMapper(TeamMapper.class);
    }
}
