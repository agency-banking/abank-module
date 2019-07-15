package com.agencybanking.messaging.queue;

import com.agencybanking.core.messaging.AppMessageType;
import com.agencybanking.core.utils.Utils;
import com.agencybanking.messaging.email.MailEngine;
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
import java.util.Date;

@Slf4j
@Component
public class AsyncMsgJob extends QuartzJobBean {
    @Autowired
    private MsgQueueRepository msgQueueRepository;
    @Autowired
    private MailEngine mailEngine;
    private int maximumRetries = 3;
    public static final Long DEFAULT_RETRY_FACTOR = 20_000L;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Page<MsgQueueData> msgPage = msgQueueRepository.findUnsent(Arrays.asList(QueueSendState.WAITING, QueueSendState.RETRY),
                PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "send_date")));
        msgPage.getContent().forEach(this::sendMessage);
    }

    private void sendMessage(MsgQueueData msg) {
        try {
            log.debug("Found Async message :{}", msg.getSubject());
            if (msg.getMsgType().equals(AppMessageType.EMAIL))
                mailEngine.send(msg);

            sendMsgSuccessful(msg);
        } catch (Exception e) {
            sendMailFailed(msg, e);
            log.error(e.getMessage(), e);
        }
    }

    private void sendMailFailed(MsgQueueData msg, Exception e) {
        msg.setSendStatus(QueueSendState.FAILED);
        msg.setFailedMsg(Utils.first(e.getMessage(), 500));

        if (msg.getRetry() && msg.getRetries() <= maximumRetries) {
            retry(msg);
        }
        msgQueueRepository.save(msg);
    }

    private void retry(MsgQueueData msg) {
        msg.retried();
        msg.setSendStatus(QueueSendState.RETRY);
        msg.setSendDate(nextSendDate(msg.getRetries()));
        log.info("Message re-queued {} times",msg.getRetries());
    }

    /**
     * f(x) = (retryfactor)x
     *
     * @param retries
     * @return
     */
    private Date nextSendDate(int retries) {
        long millisToAdd = retries * DEFAULT_RETRY_FACTOR;
        return new Date(System.currentTimeMillis() + millisToAdd);
    }

    private void sendMsgSuccessful(MsgQueueData msg) {
        msg.setSendStatus(QueueSendState.SUCCESSFUL);
        msgQueueRepository.save(msg);
    }
}
