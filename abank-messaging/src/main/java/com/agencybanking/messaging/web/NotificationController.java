package com.agencybanking.messaging.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
@CrossOrigin("*")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

//    @PostMapping("/seen")
//    public ResponseEntity<?> setSeen(@RequestBody List<Notification> data) {
//        return ResponseEntity.ok(notificationService.seen(data));
//    }
//
//    @PostMapping("/read")
//    public ResponseEntity<?> setRead(@RequestBody Notification data) {
//        return ResponseEntity.ok(notificationService.read(data));
//    }

//    @PostMapping("/search")
//    public ResponseEntity<?> loadNotifications(@RequestBody SearchRequest request) {
//        return ResponseEntity.ok(notificationService.loadNotifications(PageRequest.of(request.getPaging().getPage(),
//                request.getPaging().getLimit(), request.getPaging().getSort())));
//    }

//    @PostMapping("/searchWithCount")
//    public ResponseEntity<?> notifications(@RequestBody SearchRequest request) {
//        Page<Notification> notifications = notificationService.loadNotifications(PageRequest.of(request.getPaging().getPage(),
//                request.getPaging().getLimit(), request.getPaging().getSort()));
//        Long count = notificationService.unSeenCount();
//        Map<String, Object> notifData = new HashMap<>();
//        notifData.put("count", count);
//        notifData.put("notifications", notifications);
//        return ResponseEntity.ok(notifData);
//
//    }

//    @GetMapping("/count")
//    public ResponseEntity<?> notifCount(){
//        return ResponseEntity.ok(notificationService.unSeenCount());
//    }
}
