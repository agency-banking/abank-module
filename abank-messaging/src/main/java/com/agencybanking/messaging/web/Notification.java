package com.agencybanking.messaging.web;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**Web Notification table which will be pulled from for a user
 * @author dubic
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "AF_MSG_WEB")
public class Notification {
    @Id
    @SequenceGenerator(name = "defaultSequenceGen", initialValue = 1000, sequenceName = "AF_MSG_WEB_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "defaultSequenceGen")
    private Long id;

    @Column(name = "user_", nullable = false)
    private String user;

    @Column(name = "create_date", nullable = false)
    private Date createDate;

    @Column(name = "seen_date")
    private Date seenDate;

    @Column(name = "read_date")
    private Date readDate;

    @Column(name = "subject")
    private String subject;

    @Column(name = "url")
    private String url;

    @Column(name = "content")
    private String content;

    @Column(name = "product")
    private String product;

    @Column(name = "module")
    private String module;

    @Column(name = "ref")
    private String ref;

}
