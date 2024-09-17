package ro.axon.dot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.axon.dot.domain.repositories.LeaveReqRepository;


@Service
@RequiredArgsConstructor
public class LeaveReqService {
    private final LeaveReqRepository leaveReqRepository;

   //Impl
}
