/**
 *
 */
package com.agencybanking.core.product;

import com.agencybanking.core.data.Data;
import com.agencybanking.core.el.Label;
import com.agencybanking.core.module.Module;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author dubic
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Label(value = "Product",variable = "prod")
@EqualsAndHashCode(callSuper = true)
@lombok.Data
@Entity
@Table(name = "core_product")
public class Product extends Data {
    @Size(max = 255, message = "core.product.name.size")
    @NotBlank(message = "core.product.name.empty")
    @Column(nullable = false)
    private String name;

    @Size(message = "core.product.description.size", max = 255)
    private String description;

    @NotNull(message = "core.product.module.required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "module_code", referencedColumnName = "code")
    private Module module;

    @Id
    private String code;

    @Size(max = 300)
    @Column(name = "domains", length = 300)
    private String domains;

    @Size(max = 300)
    @Column(name = "services", length = 300)
    private String services;

    @Size(max = 300)
    @Column(name = "events_", length = 300)
    private String events;

    @Size(max = 300)
    @Column(name = "wf_events", length = 300)
    private String workflowEvents;
}