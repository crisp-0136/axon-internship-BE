package ro.axon.dot.model;

import lombok.Data;

import java.util.List;

@Data
public class TeamDetailsList {

    private List<TeamDetailsListItem> items;

    public TeamDetailsList(List<TeamDetailsListItem> items) {
        this.items = items;
    }
}
