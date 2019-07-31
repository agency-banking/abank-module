/**
 *
 */
package com.agencybanking.flow.audits;

import com.agencybanking.core.data.Active;
import com.agencybanking.core.el.Label;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @author dubic
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Active
@Label("IO Audit")
@Data
@Entity
@Table(name = "af_io_audit", indexes = {
        @Index(name = "date_index", columnList = "create_date")
})
public class IOAudit extends com.agencybanking.core.data.Data {

    @Id
    @SequenceGenerator(name = "ioAuditSequenceGen", sequenceName = "AF_IO_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ioAuditSequenceGen")
    private Long id;

    @Transient
    private Date fromDate, toDate;

    @Label("Date")
    @NotNull(message = "appflow.audit.date.required")
    @Column(name = "create_date", nullable = false)
    private Date date = new Date();

    @Lob
    @Column(name = "data")
    private String data;

    @Label("Partner")
    @Size(message = "appflow.audit.partner.size", max = 255)
    @Column(name = "partner")
    private String partner;

    @Label("Bank")
    @Size(message = "appflow.audit.partner.bank", max = 255)
    @Column(name = "bank")
    private String bank;

    @Label("Module")
    @Size(message = "appflow.audit.module.size", max = 255)
    @Column(name = "module")
    private String module;

    @Label("Product")
    @Size(max = 255, message = "appflow.audit.product.size")
    @NotBlank(message = "appflow.audit.product.empty")
    @Column(name = "product", nullable = false)
    private String product;

    @Label("Action")
    @NotNull(message = "appflow.audit.action.empty")
    @Column(name = "action", nullable = false)
    private String action;

    @Label("Transaction ID")
    @Column(name = "trans_id")
    private String transId;

    @Label("Resend")
    @Column(name = "resend")
    private Boolean resend;


    public Boolean getResend() {
        return this.resend == null ? true : this.resend;
    }

//    public BooleanBuilder predicates() {
//        QIOAudit qAudit = QIOAudit.iOAudit;
//        BooleanBuilder builder = new BooleanBuilder();
//        if (!isEmpty(this.getId())) {
//            builder.and(qAudit.id.eq(this.getId()));
//        }
////        if (!isEmpty(this.getDate())) {
////            builder.and(qAudit.date.stringValue().containsIgnoreCase(this.getDate().toString()));
////        }
////        if (!isEmpty(this.getUser())) {
////            builder.and(qAudit.user.containsIgnoreCase(this.getUser()));
////        }
//        if (!isEmpty(this.getPartner())) {
//            builder.and(qAudit.partner.containsIgnoreCase(this.getPartner()));
//        }
//        if (!isEmpty(this.getModule())) {
//            builder.and(qAudit.module.containsIgnoreCase(this.getModule()));
//        }
//        if (!isEmpty(this.getProduct())) {
//            builder.and(qAudit.product.containsIgnoreCase(this.getProduct()));
//        }
////        if (!isEmpty(this.getAction())) {
////            builder.and(qAudit.action.containsIgnoreCase(this.getAction()));
////        }
//        if (!isEmpty(this.getFromDate()) && isEmpty(this.getToDate())) {
//            builder.and(qAudit.date.between(getFromDate(), atEndOfDay(getFromDate())));
//        } else if (!isEmpty(this.getFromDate()) && !isEmpty(this.getToDate())) {
//            builder.and(qAudit.date.between(getFromDate(), getToDate()));
//        }
//        return builder;
//    }

//    /**
//     * creates predicates with Or Boolean expression
//     *
//     * @param searchRequest term recipients search all fields
//     * @return or predicates for all fields
//     */
//    public BooleanBuilder anyPredicates(SearchRequest searchRequest) {
//        QAudit qAudit = QAudit.audit;
//        BooleanBuilder builder = new BooleanBuilder();
//        if (!isEmpty(searchRequest.getSearchItem())) {
//            builder.or(qAudit.id.stringValue().containsIgnoreCase(searchRequest.getSearchItem()));
//            builder.or(qAudit.date.stringValue().containsIgnoreCase(searchRequest.getSearchItem()));
//            builder.or(qAudit.user.containsIgnoreCase(searchRequest.getSearchItem()));
//            builder.or(qAudit.partner.containsIgnoreCase(searchRequest.getSearchItem()));
//            builder.or(qAudit.module.containsIgnoreCase(searchRequest.getSearchItem()));
//            builder.or(qAudit.product.containsIgnoreCase(searchRequest.getSearchItem()));
//            builder.or(qAudit.action.containsIgnoreCase(searchRequest.getSearchItem()));
//        }
//        return builder;
//    }
}
