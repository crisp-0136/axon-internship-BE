package ro.axon.dot.model;

import lombok.Data;

import java.util.List;

@Data
public class LeaveReqDetailsList {
    List<LeaveReqDetailsListItems> items;
}
