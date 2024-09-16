package ro.axon.dot.model;

import lombok.Data;

import java.util.List;

@Data
public class LeaveReqList {

    private List<LeaveReqItem> items;
}
