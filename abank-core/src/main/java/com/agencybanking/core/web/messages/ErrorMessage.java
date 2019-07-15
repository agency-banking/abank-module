package com.agencybanking.core.web.messages;

import com.agencybanking.core.utils.Utils;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

import static com.agencybanking.core.data.Data.PACKAGE;

@Data
@Entity
@Table(name = "core_errors")
@SequenceGenerator(name = "defaultSequenceGen", initialValue = 1000, sequenceName = "CORE_ERRORS_SEQ", allocationSize = 1)
public class ErrorMessage extends Message {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "defaultSequenceGen")
    private Long id;


    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false)
    private Date createDate;

    @Version
    private Long version;

    private String mainEx;
    private String trace;
    private String causeEx;
    @NotNull
    private Boolean fixed;
    @Lob
    private String form;

    /**
     * @param fullCode e.g success.mbcp-201
     * @param msg
     */
    public ErrorMessage(String fullCode, String msg, MessageType type) {
        super(msg, type);
        String[] codes = fullCode.split("\\.");
        if (codes.length > 1) {
            this.msgCode = codes[1].toUpperCase();
        } else {
            this.msgCode = fullCode;
        }
    }

    public ErrorMessage exception(Throwable e) {
        this.mainEx = Utils.first(e.getClass().getName() + ":" + e.getMessage(), 255);
        Throwable cause = e.getCause();
        if (cause != null) {
            this.causeEx = Utils.first(cause.getClass().getName() + ":" + cause.getMessage(), 255);
        }
        int giveup = 0;
        for (StackTraceElement stack : e.getStackTrace()) {
            if (stack.getClassName().startsWith(PACKAGE) || giveup == 20) {
                this.trace = String.format("%s.%s(%d)", stack.getClassName(), stack.getMethodName(), stack.getLineNumber());
                System.out.println("STACK DESIRED GOTTEN : " + this.trace);
                break;
            }
            giveup++;
        }
        return this;
    }
}
