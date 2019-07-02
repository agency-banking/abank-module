/**
 *
 */
package com.agencybanking.core.module;

import com.agencybanking.core.el.Label;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author dubic
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Label("Module")
@Data
@Entity
@Table(name = "core_module")
public class Module extends com.agencybanking.core.data.Data {
    public static final String MODULE_CORE = "core";
    public static final String MODULE_SECURITY = "security";
    public static final String MODULE_EFORMS = "eforms";
    public static final String MODULE_FX = "fxpurchase";
    public static final String MODULE_LC = "lc";
    public static final String MODULE_LICENSE = "lc";
    public static final String MODULE_CUSTOMSDUTY = "customsduty";
    public static final String MODULE_BUSINESS = "business";
    public static final String MODULE_DOCUMENT = "document";
    public static final String MODULE_BC = "bc";
    public static final String MODULE_APPFLOW = "appflow";
    public static final String APPFLOW = "appflow";

    @Size(message = "core.module.name.size", max = 255)
    @NotBlank(message = "core.module.name.empty")
    @Column(nullable = false)
    private String name;

    @Size(max = 255, message = "core.module.description.size")
    private String description;

    @Id
    private String code;

    /**
     * can be licensed recipients users
     */
    @Column(name = "license_fg")
    private boolean license = false;

}