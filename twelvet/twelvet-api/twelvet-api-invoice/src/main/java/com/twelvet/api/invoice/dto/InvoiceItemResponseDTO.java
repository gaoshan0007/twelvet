package com.twelvet.api.invoice.dto; // Corrected package

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for representing an invoice item in responses.
 *
 * @author TwelveT
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemResponseDTO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 货物或应税劳务、服务名称
     */
    private String itemName;

    /**
     * 规格型号 (optional)
     */
    private String specificationModel;

    /**
     * 单位 (optional)
     */
    private String unit;

    /**
     * 数量
     */
    private BigDecimal quantity;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 金额 (不含税)
     */
    private BigDecimal amountBeforeTax;

    /**
     * 税率 (e.g., 0.13 for 13%)
     */
    private BigDecimal taxRate;

    /**
     * 税额
     */
    private BigDecimal taxAmount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 备注 (from BaseEntity, if applicable to items)
     */
    private String remark;
}
