package com.agencybanking.messaging.email;

import com.agencybanking.messaging.MessagingModule;
import com.agencybanking.messaging.queue.QueueSendState;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
public class AsyncMailJob extends QuartzJobBean {
    @Autowired
    private MailQueueRepository mailQueueRepository;
    @Autowired
    private MailEngine mailEngine;
    @Autowired
    private MessagingModule module;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Page<MailQueueData> msgPage = loadUnsentMsgs();
        msgPage.getContent().forEach(this::sendMessage);
    }

    public Page<MailQueueData> loadUnsentMsgs() {
        return mailQueueRepository.findUnsent(Arrays.asList(QueueSendState.WAITING, QueueSendState.RETRY),
                PageRequest.of(0, module.getMail().getFetchSize(), Sort.by(Sort.Direction.ASC, "sendDate")));
    }

    private void sendMessage(MailQueueData msg) {
        try {
            log.debug("Found Async message :{}", msg.getSubject());
            mailEngine.send(msg);

            msg.successful();
        } catch (Exception e) {
            failed(msg,e);
        } finally {
            mailQueueRepository.save(msg);
        }
    }

    public void failed(MailQueueData msg, Exception e) {
        msg.failed(e, module);
        msg.doRetry(module.getMail().getMaximumRetries(), module.getMail().getRetryFactor());
        log.error(e.getMessage(), e);
    }

}
