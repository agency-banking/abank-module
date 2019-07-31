/**
 *
 */
package com.agencybanking.security.auth.providers.ldap;

import com.agencybanking.core.data.Active;
import com.agencybanking.core.data.BaseEntity;
import com.agencybanking.core.el.Label;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author dubic
 */
@Active
@Label("LDAP Details")
@Data
@Entity
@Table(name = "sec_auth_ldap")
@SequenceGenerator(name = "defaultSequenceGen", sequenceName = "SEC_AUTH_LDAP_SEQ", allocationSize = 1)
public class LdapDetails extends BaseEntity {

    @Label("Company Id")
    @NotNull(message = "security.ldapdetails.companyid.required")
    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Override
    public String forCode() {
        return companyId.toString();
    }

}