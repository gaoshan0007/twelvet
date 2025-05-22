package com.twelvet.server.invoice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.twelvet.framework.core.application.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;

/**
 * 发票项目实体
 * Represents an item line on a Chinese VAT invoice.
 *
 * @author TwelveT
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "invoice_item")
public class InvoiceItem extends BaseEntity {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id")
    private Long id;

    /**
     * 关联的发票主体
     */
    @JsonIgnore // Avoid circular dependency in serialization if Invoice also serializes items
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    /**
     * 货物或应税劳务、服务名称
     */
    @Column(name = "item_name", nullable = false, length = 255)
    private String itemName;

    /**
     * 规格型号 (optional)
     */
    @Column(name = "specification_model", length = 100)
    private String specificationModel;

    /**
     * 单位 (optional)
     */
    @Column(name = "unit", length = 50)
    private String unit;

    /**
     * 数量
     */
    @Digits(integer = 18, fraction = 6) // Allowing for more precision in quantity if needed
    @Column(name = "quantity", nullable = false, precision = 24, scale = 6)
    private BigDecimal quantity;

    /**
     * 单价
     */
    @Digits(integer = 18, fraction = 6) // Allowing for more precision in unit price
    @Column(name = "unit_price", nullable = false, precision = 24, scale = 6)
    private BigDecimal unitPrice;

    /**
     * 金额 (不含税) = quantity * unitPrice
     */
    @Digits(integer = 18, fraction = 2)
    @Column(name = "amount_before_tax", nullable = false, precision = 20, scale = 2)
    private BigDecimal amountBeforeTax;

    /**
     * 税率 (e.g., 0.13 for 13%)
     */
    @Digits(integer = 3, fraction = 4) // e.g., 0.1300 or 0.0150
    @Column(name = "tax_rate", nullable = false, precision = 7, scale = 4)
    private BigDecimal taxRate;

    /**
     * 税额 = amountBeforeTax * taxRate
     */
    @Digits(integer = 18, fraction = 2)
    @Column(name = "tax_amount", nullable = false, precision = 20, scale = 2)
    private BigDecimal taxAmount;

    // createTime, updateTime, createBy, updateBy, remark are inherited from BaseEntity
    // For InvoiceItem, createBy and updateBy might be less relevant if items are always managed
    // in the context of the parent Invoice. However, using BaseEntity provides them.
}
