package ro.axon.dot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.axon.dot.domain.TeamEty;
import ro.axon.dot.domain.enums.Status;
import ro.axon.dot.domain.repositories.TeamRepository;
import ro.axon.dot.mapper.TeamMapper;
import ro.axon.dot.model.TeamDetailsList;
import ro.axon.dot.model.TeamDetailsListItem;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;


    @Transactional(readOnly = true)
    public TeamDetailsList getActiveTeamDetails() {
        List<TeamEty> activeTeams = teamRepository.findAll().stream()
                .filter(team -> team.getStatus() == Status.ACTIVE)
                .toList();

        List<TeamDetailsListItem> teamDetails = activeTeams.stream()
                .map(teamMapper::mapTeamEtyToTeamDto)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new TeamDetailsList(teamDetails);
    }



}
