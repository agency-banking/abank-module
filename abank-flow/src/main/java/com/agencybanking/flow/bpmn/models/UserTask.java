package com.agencybanking.flow.bpmn.models;

import com.agencybanking.core.data.Period;
import com.agencybanking.core.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.time.LocalDateTime;
import java.util.Date;

import static org.springframework.util.Assert.hasLength;
import static org.springframework.util.ObjectUtils.isEmpty;

/**
 * @author dubic
 */
//@Json
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTask extends BPMNNode {

    public static final Integer PRIORITY_HIGH = 1;
    public static final Integer PRIORITY_MEDIUM = 2;
    public static final Integer PRIORITY_LOW = 3;

    @XmlElement(name = "documentation")
    private Documentation documentation;

    @XmlAttribute(name = "dueAfter")
    private Integer dueAfter;

    @XmlAttribute(name = "dueAfterPeriod")
    private Period dueAfterPeriod;

    @XmlAttribute(name = "assignee")
    private String assignee;

    @XmlAttribute(name = "candidateUsers")
    private String potentialOwners;

    @XmlAttribute(name = "candidateRoles")
    private String roles;

    @XmlAttribute(name = "candidateGroups")
    private String groups;
    @XmlElement(name = "formKey")
    private String formKey;

    @XmlAttribute(name = "priority")
    private Integer priority;

    @XmlElement(name = "escalate")
    private String escalate;

    @XmlElement(name = "notifyOwners")
    private boolean notifyOwners;

    @XmlElement(name = "notifyOriginator")
    private boolean notifyOriginator;

    @XmlElement(name = "rejectTo")
    private String reject;

    @XmlElement(name = "originator")
    private String originator;

    @XmlTransient
    private String assigneeName;
    @XmlTransient
    private String potentialOwnerNames;
    @XmlTransient
    private String roleNames;
    @XmlTransient
    private String groupNames;

    @Override
    public boolean pauseExecution() {
        return true;
    }

    @Override
    public void validate() {
        super.validate();
        hasLength(getName(), "User task must have a name");
        hasLength(formKey, "User task must have a form key");
        if (isEmpty(assignee) && isEmpty(potentialOwners) && isEmpty(roles) && isEmpty(groups)) {
            throw new IllegalArgumentException("UserTask must be assigned recipients a user or role or group");
        }
    }

    public Date calculateEscalationDate() {
        if (this.dueAfter == null)
            return null;
        if (Period.DAYS.equals(dueAfterPeriod)) {
            return DateUtils.localDateTimeToDate(LocalDateTime.now().plusDays(dueAfter));
        } else {
            return DateUtils.localDateTimeToDate(LocalDateTime.now().plusHours(dueAfter));
        }
    }
}
