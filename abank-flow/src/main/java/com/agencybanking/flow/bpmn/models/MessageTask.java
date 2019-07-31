package com.agencybanking.flow.bpmn.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.persistence.oxm.annotations.XmlCDATA;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.Assert.hasLength;

/**
 * Represents an email,sms, or web notification task
 *
 * @author dubic
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageTask extends BPMNNode {

    @XmlElement(name = "messageType")
    private MessageType messageType;

    @XmlElement(name = "template")
    private String template;

    @XmlCDATA
    @XmlElement(name = "text")
    private String text;

    @XmlElement(name = "models")
    private List<String> models = new ArrayList<>();

    @XmlElement(name = "users")
    private List<String> users = new ArrayList<>();

    @XmlElement(name = "roles")
    private List<String> roles = new ArrayList<>();

    @XmlElement(name = "groups")
    private List<String> groups = new ArrayList<>();

    @XmlElement(name = "copies")
    private String cc;

    @XmlElement(name = "blind-copies")
    private String bcc;

    @XmlCDATA
    @XmlElement(name = "subject")
    private String subject;

    @XmlElement(name = "attachment-url")
    private String attachmentUrl;

    @XmlElement(name = "attachment-name")
    private String attachmentName;

    @XmlElement(name = "attachment-key")
    private String attachmentKey;

    @XmlAttribute
    private boolean async;
    @XmlAttribute
    private String product;
    @XmlAttribute
    private String module;
    @XmlAttribute
    private String ref;

    @XmlAttribute
    private int resend;

    @XmlTransient
    private String message;
    @XmlTransient
    private String templateName;

    @Override
    public void validate() {
        super.validate();
        Assert.notNull(messageType, "MessageTask must have a messageType");

        if (messageType == MessageType.EMAIL) {
            hasLength(subject, "Subject is required");
            if (ObjectUtils.isEmpty(template) && ObjectUtils.isEmpty(text)) {
                throw new IllegalArgumentException("MessageTask must have a text or template");
            }
            models.addAll(users);
            models.addAll(roles);
            models.addAll(groups);
            Assert.notEmpty(models, "MessageTask must have a recipient. consider adding models,users,roles");
        }
        if (messageType == MessageType.WEB) {
            hasLength(subject, "Subject is required");
            hasLength(text, "Text is required for Web messages");
            users.addAll(roles);
            models.addAll(groups);
            Assert.notEmpty(users, "MessageTask must have a recipient. consider adding users,roles");
        }
    }
}
