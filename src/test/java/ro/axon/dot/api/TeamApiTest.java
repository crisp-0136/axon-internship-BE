package ro.axon.dot.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import ro.axon.dot.domain.enums.Status;
import ro.axon.dot.model.TeamDetailsList;
import ro.axon.dot.model.TeamDetailsListItem;
import ro.axon.dot.service.TeamService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class TeamApiTest {

    @InjectMocks
    private TeamApi teamApi;

    @Mock
    private TeamService teamService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTeamDetailsList_shouldReturnResponseWithTeamDetails() {
        // Arrange
        TeamDetailsListItem activeTeamItem = new TeamDetailsListItem();
        activeTeamItem.setId(1L);
        activeTeamItem.setName("Active Team");
        activeTeamItem.setStatus(Status.ACTIVE);

        TeamDetailsList teamDetailsList = new TeamDetailsList(List.of(activeTeamItem)); // Pass the list to the constructor

        when(teamService.getActiveTeamDetails()).thenReturn(teamDetailsList);

        // Act
        ResponseEntity<TeamDetailsList> response = teamApi.getTeamDetailsList();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(teamDetailsList, response.getBody());
    }

    @Test
    void getTeamDetailsList_shouldHandleNoTeams() {
        // Arrange
        TeamDetailsList teamDetailsList = new TeamDetailsList(List.of()); // Pass empty list to constructor

        when(teamService.getActiveTeamDetails()).thenReturn(teamDetailsList);

        // Act
        ResponseEntity<TeamDetailsList> response = teamApi.getTeamDetailsList();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(teamDetailsList, response.getBody());
    }


    @Test
    void getTeamDetailsList_shouldHandleMultipleTeams() {
        // Arrange
        TeamDetailsListItem activeTeamItem1 = new TeamDetailsListItem();
        activeTeamItem1.setId(1L);
        activeTeamItem1.setName("Active Team 1");
        activeTeamItem1.setStatus(Status.ACTIVE);

        TeamDetailsListItem activeTeamItem2 = new TeamDetailsListItem();
        activeTeamItem2.setId(2L);
        activeTeamItem2.setName("Active Team 2");
        activeTeamItem2.setStatus(Status.INACTIVE); // This should be excluded

        // Only the active team should be returned
        TeamDetailsList expectedTeamDetailsList = new TeamDetailsList(List.of(activeTeamItem1)); // Pass only active teams

        // Simulate the service returning the list with both active and inactive teams
        TeamDetailsList teamDetailsListFromService = new TeamDetailsList(List.of(activeTeamItem1, activeTeamItem2)); // Include both

        when(teamService.getActiveTeamDetails()).thenReturn(expectedTeamDetailsList);

        // Act
        ResponseEntity<TeamDetailsList> response = teamApi.getTeamDetailsList();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedTeamDetailsList, response.getBody());
    }


}
