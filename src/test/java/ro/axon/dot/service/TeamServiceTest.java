package ro.axon.dot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ro.axon.dot.domain.TeamEty;
import ro.axon.dot.domain.enums.Status;
import ro.axon.dot.domain.repositories.TeamRepository;
import ro.axon.dot.mapper.TeamMapper;
import ro.axon.dot.model.TeamDetailsList;
import ro.axon.dot.model.TeamDetailsListItem;
import ro.axon.dot.service.TeamService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

class TeamServiceTest {

    @InjectMocks
    private TeamService teamService;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeamMapper teamMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks
    }

    @Test
    void getActiveTeamDetails_shouldReturnOnlyActiveTeams() {
        // Setup the active team
        TeamEty activeTeam = new TeamEty();
        activeTeam.setStatus(Status.ACTIVE);
        activeTeam.setName("Active Team");

        // Mock the repository to return only active teams
        when(teamRepository.findByStatus(Status.ACTIVE)).thenReturn(List.of(activeTeam));

        // Setup the mapped DTO
        TeamDetailsListItem activeTeamDto = new TeamDetailsListItem();
        activeTeamDto.setName("Active Team");

        // Mock the mapper to return the DTO for the active team
        when(teamMapper.mapTeamEtyToTeamDto(activeTeam)).thenReturn(activeTeamDto);

        // Call the service method
        TeamDetailsList result = teamService.getActiveTeamDetails();

        // Assert the results
        List<TeamDetailsListItem> items = result.getItems();
        assertFalse(items.isEmpty());
        assertEquals(1, items.size());
        assertEquals("Active Team", items.get(0).getName());
    }
}
