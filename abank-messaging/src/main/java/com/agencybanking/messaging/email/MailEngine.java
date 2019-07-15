package com.agencybanking.messaging.email;

import com.agencybanking.core.storage.ResourceProvider;
import com.agencybanking.core.utils.Utils;
import com.agencybanking.messaging.MessagingModule;
import com.agencybanking.messaging.queue.MsgQueueData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.mail.internet.MimeMessage;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@Component
public class MailEngine {
//    private final IdentityService identityService;
    private final ResourceProvider resourceProvider;
    private final MessagingModule module;
//    @Value("${spring.mail.username}")
//    private String mailSender;
//    @Value("${mail.sendername}")
//    private String mailSenderName;
//
//
    private final JavaMailSender sender;

    public MailEngine(JavaMailSender sender, ResourceProvider resourceProvider, MessagingModule module) {
        this.sender = sender;
        this.resourceProvider = resourceProvider;
        this.module = module;
    }

    public void send(MsgQueueData mail) throws Exception {
        doSend(mail);
    }

    @Async
    public void sendAsync(MsgQueueData mail) throws Exception {
        doSend(mail);
    }

    private void doSend(MsgQueueData mail) throws Exception {
        MimeMessage mime = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mime, true);

        helper.setText(mail.getText(), true);

        helper.setTo(Utils.fromCommaStringToArray(mail.getRecipients()));
        helper.setSubject(mail.getSubject());
        helper.setCc(Utils.fromCommaStringToArray(mail.getCopies()));
        helper.setBcc(Utils.fromCommaStringToArray(mail.getBlanks()));
        helper.setFrom(module.getMail().getSender(), module.getMail().getSenderName());

        Resource resource = getAttachmentKeyResource(mail);
        if (resource != null) {
            helper.addAttachment(mail.getAttachmentName(), resource);
        }
        sender.send(mime);
        log.info("mail sent successfully [{}], [{}]", mail.getSubject(), mail.getRecipients());
    }
//
    public Resource getAttachmentKeyResource(MsgQueueData msg) throws URISyntaxException, MalformedURLException {
        if (!StringUtils.isEmpty(msg.getAttachmentUrl())){
            return getAttachmentResource(msg.getAttachmentUrl());
        }
        if (ObjectUtils.isEmpty(msg.getAttachmentKey())) {
            return null;
        }
        log.debug("Processing Resource for key : " + msg.getAttachmentKey());
        return resourceProvider.resource(msg.getAttachmentKey());
    }
//
    public Resource getAttachmentResource(String url) throws URISyntaxException, MalformedURLException {
        log.debug("Processing Resource for : " + url);
        if (url == null) {
            return null;
        }
        url = url.replaceAll(" ", "%20");
        URI uri = new URI(url);
        log.debug("scheme parsed : " + uri.getScheme());
        if ("file".equalsIgnoreCase(uri.getScheme())) {
            log.debug("File system Resource : " + url);
            return new FileSystemResource(uri.getPath());
        }
        if ("http".equalsIgnoreCase(uri.getScheme()) || "https".equalsIgnoreCase(uri.getScheme())) {
            log.debug("URL Resource : " + url);
            return new UrlResource(uri);
        }
        return new FileSystemResource(url);
    }
}
