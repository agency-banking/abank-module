package com.agencybanking.messaging.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationService {
//    private final NotificationRepository repository;
//    private final IdentityService identityService;
//    private final PushService pushService;

//    public NotificationService(NotificationRepository repository, IdentityService identityService,
//                               SimpMessagingTemplate websocket, PushService pushService) {
//        this.repository = repository;
//        this.identityService = identityService;
//        this.pushService = pushService;
//    }

    /**
     * @param notification web message data without recipient
     * @param usernames    Set of unique usernames
     * @return the saved notification(s)
     */
//    public List<Notification> save(Notification notification, Set<String> usernames) {
//        List<Notification> notifications = new ArrayList<>();
//
//        usernames.stream().filter(u -> !StringUtils.isEmpty(u)).forEach(username -> {
//            notification.setId(null);
//            notification.setUser(username);
//            notifications.add(saveAndPush(notification));
//        });
//
//        log.info("Notification saved for {}", usernames.stream().collect(Collectors.joining(",")));
//        return notifications;
//    }

//    private Notification saveAndPush(Notification notification) {
//        Notification saved = this.repository.save(notification);
//        push(saved);
//        return saved;
//    }
//
//    public void push(Notification n) {
//        Assert.notNull(n, "Notification must not be null");
//        this.pushService.push(n, n.getUser(), PushService.PATH_NOTIFICATION);
//    }
//
//    public Page<Notification> findAvailableNotifications(PageRequest page) {
//        return this.repository.findBySeenDateIsNullAndReadDateIsNull(page);
//    }
//
//    public boolean seen(List<Notification> notifications) {
//        Date seenDate = new Date();
//        for (Notification notification : notifications){
//            if (ObjectUtils.isEmpty(notification.getSeenDate())){
//                notification.setSeenDate(seenDate);
//                repository.save(notification);
//            }
//        }
//        return true;
//    }
//
//    public Page<Notification> loadNotifications(PageRequest p) {
//        String user = identityService.getAuthenticatedUser();
//        return repository.findByUser(user, p);
//    }
//
//    public Notification read(Notification notification) {
//        Date readDate = new Date();
//        if(ObjectUtils.isEmpty(notification.getReadDate())) {
//            notification.setReadDate(readDate);
//            return repository.save(notification);
//        }
//        return notification;
//
//    }
//
//    public Long unSeenCount(){
//        return repository.countBySeenDateIsNull();
//    }
//
//    public Collection<? extends String> rolesToUsernames(List<String> roles) {
//        List<String> users = new ArrayList<>();
//        for (String roleRef : roles) {
//            users.addAll(this.identityService.getUsersWithRole(roleRef));
//        }
//        return users;
//    }
//
//    public Collection<? extends String> groupsToUsernames(List<String> groups) {
//        List<String> users = new ArrayList<>();
//        for (String group : groups) {
//            users.addAll(this.identityService.getUsersInGroup(group));
//        }
//        return users;
//    }
}
