package com.agencybanking.security.auth.providers.ldap;

import com.agencybanking.core.services.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author dubic 
 *
 */
@Service
@Slf4j
public class LdapDetailsService extends BaseService {

	private LdapDetailsRepository ldapDetailsRepository;
    private final RestTemplate restTemplate;

	public LdapDetailsService(LdapDetailsRepository ldapDetailsRepository,RestTemplateBuilder restTemplateBuilder){
		this.ldapDetailsRepository = ldapDetailsRepository;
        this.restTemplate = restTemplateBuilder.build();
	}

}
