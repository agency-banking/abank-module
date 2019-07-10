package com.agencybanking.agents;

import org.springframework.context.ApplicationEventPublisher;
import java.util.Optional;
import org.springframework.util.ObjectUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.security.access.annotation.Secured;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import com.unionsystems.mbcp.core.business.EventType;
import com.unionsystems.mbcp.core.services.ModelExistsException;
import com.unionsystems.mbcp.agents.AgentsMessages;
import static com.unionsystems.mbcp.security.auth.AuthenticationService.companyAuditDetails;
import com.agencybanking.core.services.Crud;
import com.agencybanking.core.data.Money;
import com.unionsystems.mbcp.core.web.messages.MsgContextHolder;
import com.unionsystems.mbcp.core.services.MbcpEvent;
import com.unionsystems.mbcp.core.data.SearchRequest;

/**
 * @author dubic 
 *
 */
@Service
@Slf4j
public class AgentService implements Crud<Agent, Long>{

	private AgentRepository agentRepository;
    private final ApplicationEventPublisher publisher;

	public AgentService(AgentRepository agentRepository,
                                     ApplicationEventPublisher publisher){
		this.agentRepository = agentRepository;
        this.publisher = publisher;
	}
//Agent Messages
public static final String ERR_AGENT_EXISTS = "err.mbcp-";
public static final String SUCCESS_AGENT_CREATED = "suc.mbcp-";
public static final String SUCCESS_AGENT_UPDATED = "suc.mbcp-";
public static final String SUCCESS_AGENT_DELETED = "suc.mbcp-";

	/**Creates a new record of Agent
	 *Checks for existing record by phone,email
	 * @param agent like from new ()
	 * @return the created record
	 */
	@Override
    @Secured("ROLE_CREATE_AGENT")
	public Agent create(Agent agent) {
        runRules(agent, EventType.CREATE);
        agent.validate();
        if (exists(agent, null)) {
            throw new ModelExistsException(AgentsMessages.ERR_AGENT_EXISTS, agent.getName());
        }
        Agent saved = this.agentRepository.save(agent);
        MsgContextHolder.getInstance(saved).success(AgentsMessages.SUCCESS_AGENT_CREATED, saved.getName());
        this.publisher.publishEvent(MbcpEvent.of(saved).evt(EventType.CREATE).ref(saved.getId()));
        return saved;
	}

    @Secured("ROLE_MODIFY_AGENT")
    @Override
    public Agent update(Agent agent) {
        Agent found = this.agentRepository.findById(agent.getId()).orElseThrow(() -> new IllegalStateException("Resource not found by id"));
        found.copyForUpdate(agent);
        runRules(agent, EventType.MODIFY);
        agent.validate();
        if (exists(found, found.getId())) {
            throw new ModelExistsException(AgentsMessages.ERR_AGENT_EXISTS, found.getName());
        }
        Agent saved = this.agentRepository.save(found);
        MsgContextHolder.getInstance(saved).success(AgentsMessages.SUCCESS_AGENT_UPDATED, saved.getName());
        this.publisher.publishEvent(MbcpEvent.of(saved).evt(EventType.MODIFY).ref(saved.getId()));
        return saved;
    }

    @Secured("ROLE_DELETE_AGENT")
	@Override
	public void delete(Agent agent) {
        runRules(agent, EventType.DELETE);
        agentRepository.delete(agent);
        MsgContextHolder.getInstance(agent).success(AgentsMessages.SUCCESS_AGENT_DELETED);
        this.publisher.publishEvent(MbcpEvent.of(agent).evt(EventType.DELETE).ref(agent.getId()));
	}

    @Secured("ROLE_VIEW_AGENT")
	@Override
	public Optional<Agent> findById(Long id) {
		return agentRepository.findById(id);
	}

	@Override
	public Page<Agent> query(Agent agent, PageRequest p) {
		if (ObjectUtils.isEmpty(agent)) {
			return agentRepository.findAll(p);
		}
		return agentRepository.findAll(Example.of(agent), p);
	}

	@Override
	public boolean exists(Agent agent, Long id) {
        AuditDetails auditDetails = AuthenticationService.currentAuditDetails();
        if (id == null) {
            return agentRepository.findByPhoneAndEmailAndAuditDetailsCmpCode(agent.getPhone(),agent.getEmail(),auditDetails.getCmpCode()).isPresent();
        } else {
            return agentRepository.findByPhoneAndEmailAndAuditDetailsCmpCodeAndIdNot(agent.getPhone(),agent.getEmail(),auditDetails.getCmpCode(),id).isPresent();
        }
	}

    @Override
    public Page<Agent> findAll(Agent agent, PageRequest p) {
        return agentRepository.findAll(agent.predicates(), p);
    }

    @Override
    public Page<Agent> singleSearch(SearchRequest request, PageRequest p) {
        return agentRepository.findAll(new Agent().anyPredicates(request), p);
    }

    @Override
    public Page<Agent> single(String... args) {
        return null;
    }

    /**defines if a given operation (by event) can be executed by simple rules
     * @param agent
     * @param event
     * @throws MbcpServiceException if validation fails
     */
    private void runRules(Agent agent, EventType event) {
        switch (event) {
            case MODIFY:
                if ("A".equals("AS")) {
                    throw new MbcpServiceException(SecurityMessages.ERR_BAD_CREDENTIALS, "A");
                }
                break;
            case DELETE:
                break;
            case CREATE:
                break;

        }
    }
}
