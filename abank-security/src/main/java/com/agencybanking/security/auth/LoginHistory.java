package com.agencybanking.security.auth;

import com.agencybanking.core.services.AppContextHolder;
import com.agencybanking.core.utils.Utils;
import com.agencybanking.security.users.User;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sec_login_hist")
public class LoginHistory {

    @Column(name = "login_id", nullable = false)
    private String loginId;

    @Column(name = "login_ip", length = 20)
    private String ip;

    @Column(name = "login_time", nullable = false)
    private LocalDateTime lastLogin = LocalDateTime.now();

    @Column(name = "location")
    private String location;

    @Column(name = "device")
    private String device;

    @Column(name = "success", nullable = false)
    private Boolean success = Boolean.TRUE;

    @Column(name = "message")
    private String message;

    @Id
    @SequenceGenerator(name = "defaultSequenceGen", sequenceName = "SEC_LOGIN_HIST_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "defaultSequenceGen")
    private Long id;

    public static LoginHistory of(User user) {
        LoginHistory h = new LoginHistory();
        h.setLoginId(SecurityHolder.getInstance().getAuthRequest().getPrincipal().getLoginId());
        AppContextHolder ctxHolder = AppContextHolder.getInstance();
        h.setIp(ctxHolder.getRequest().getRemoteAddr());
        h.setDevice(Utils.first(ctxHolder.getUserAgent(),254));
        return h;
    }
}
