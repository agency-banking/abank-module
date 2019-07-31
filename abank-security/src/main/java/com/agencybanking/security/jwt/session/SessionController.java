package com.agencybanking.security.jwt.session;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/session")
public class SessionController {

    private SessionStore sessionStore;

    public SessionController(SessionStore sessionService) {
        this.sessionStore = sessionService;
    }

//    @PostMapping("/list")
//    public ResponseEntity<?> search(@RequestBody SearchRequest request) {
//        return ResponseEntity.ok(sessionStore.singleSearch(request, PageRequest.of(request.getPaging().getPage(),
//                request.getPaging().getLimit(), request.getPaging().getSort())));
//    }
//
//    @PostMapping("/list")
//    public ResponseEntity<?> findAll(@RequestBody Session session) {
//        return ResponseEntity.ok(sessionStore.getCurrentSessions(session.getPaging().getPageRequest()));
//    }

}
