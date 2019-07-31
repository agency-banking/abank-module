/**
 *
 */
package com.agencybanking.security.tokens;

import com.agencybanking.core.el.Label;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author dubic
 */
@Label("Token")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "sec_tokens")
@SequenceGenerator(name = "defaultSequenceGen", initialValue = 1000, sequenceName = "SEC_TOKENS_SEQ", allocationSize = 1)
public class Token extends com.agencybanking.core.data.Data {

    @Id
    @SequenceGenerator(name = "defaultSequenceGen", sequenceName = "HIBERNATE_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "defaultSequenceGen")
    private Long id;

    @NotBlank(message = "core.token.token.empty")
    @Size(message = "core.token.token.size", max = 255)
    @Column(name = "token", nullable = false)
    @Label("Token")
    private String token;

    @Column(name = "data_id")
    private Long dataId;

    @Column(name = "data_ref")
    private String dataRef;

    @NotNull(message = "security.token.createdate.required")
    @Column(name = "create_date", nullable = false)
    @Label("Created Date")
    private Date createDate;

    @Column(name = "used_date")
    private Date usedDate;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @Column(name = "token_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @Column(name = "used")
    private Boolean used;


    public void invalidate() {
        this.usedDate = new Date();
        this.used = true;
    }

    public boolean isExpired() {
        if (this.expiryDate == null) return false;
        return LocalDateTime.now().isAfter(this.expiryDate);
    }

    public Boolean getUsed() {
        return used == null ? false : used;
    }


}
