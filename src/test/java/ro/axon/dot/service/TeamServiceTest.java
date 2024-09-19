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
        // Setup mock data
        TeamEty activeTeam = new TeamEty();
        activeTeam.setStatus(Status.ACTIVE);
        activeTeam.setName("Active Team");

        TeamEty inactiveTeam = new TeamEty();
        inactiveTeam.setStatus(Status.INACTIVE);
        inactiveTeam.setName("Inactive Team");

        // Mock repository call
        when(teamRepository.findAll()).thenReturn(List.of(activeTeam, inactiveTeam));

        // Mock mapper call
        TeamDetailsListItem activeTeamDto = new TeamDetailsListItem();
        activeTeamDto.setName("Active Team");

        when(teamMapper.mapTeamEtyToTeamDto(activeTeam)).thenReturn(activeTeamDto);
        when(teamMapper.mapTeamEtyToTeamDto(inactiveTeam)).thenReturn(null); // Ensures inactive team is not included

        // Call the method
        TeamDetailsList result = teamService.getActiveTeamDetails();

        // Check the results
        List<TeamDetailsListItem> items = result.getItems();
        assertFalse(items.isEmpty());
        assertEquals(1, items.size());
        assertEquals("Active Team", items.get(0).getName());
    }
}
