/**
 *
 */
package com.agencybanking.security.auth.providers.tokenserver;

import com.agencybanking.core.data.Active;
import com.agencybanking.core.el.Label;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author dubic
 */
@Active
@Label("Token Server Authentication")
@Data
@Entity
@Table(name = "sec_auth_tokenserver")
public class TokenServer extends com.agencybanking.core.data.Data{

    @Id
    private Long id;

    @Label("URL")
    @Size(message = "security.tokenserver.url.size", max = 300)
    @NotBlank(message = "security.tokenserver.url.empty")
    @Column(name = "url", nullable = false)
    private String url;

    @Label("Post Params")
    @Size(message = "security.tokenserver.postparams.size", max = 300)
    @Column(name = "post_params")
    private String postParams;

    @Label("Return Value")
    @NotBlank(message = "security.tokenserver.returns.empty")
    @Size(message = "security.tokenserver.returns.size", max = 255)
    @Column(name = "returns", nullable = false)
    private String returns;

    @Label("HTTP Authentication Name")
    @Size(max = 255, message = "security.tokenserver.httpauthname.size")
    @Column(name = "http_auth_name")
    private String httpAuthName;

    @Label("HTTP Authentication Password")
    @Size(message = "security.tokenserver.httpauthpwd.size", max = 255)
    @Column(name = "http_auth_pwd")
    private String httpAuthPwd;
}