package org.example.admin;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RequestService {

    private final RequestDAO requestDAO;

    public List<Request> getRequest (){
        return requestDAO.getAllRequest();
    }
}
