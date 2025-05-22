package com.twelvet.api.invoice.dto; // Corrected package

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for representing a full invoice in responses.
 *
 * @author TwelveT
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponseDTO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 发票代码 (fāpiào dàimǎ)
     */
    private String invoiceCode;

    /**
     * 发票号码 (fāpiào hàomǎ)
     */
    private String invoiceNumber;

    /**
     * 开票日期 (kāipiào rìqī)
     */
    private LocalDate invoiceDate;

    /**
     * 校验码 (jiàoyàn mǎ)
     */
    private String checkCode;

    /**
     * 机器编号 (jīqì biānhào)
     */
    private String machineNumber;

    /**
     * 购买方名称
     */
    private String purchaserName;

    /**
     * 购买方纳税人识别号
     */
    private String purchaserTin;

    /**
     * 购买方地址、电话
     */
    private String purchaserAddressPhone;

    /**
     * 购买方开户行及账号
     */
    private String purchaserBankAccount; // Corrected spelling from prompt

    /**
     * 销售方名称
     */
    private String sellerName;

    /**
     * 销售方纳税人识别号
     */
    private String sellerTin;

    /**
     * 销售方地址、电话
     */
    private String sellerAddressPhone;

    /**
     * 销售方开户行及账号
     */
    private String sellerBankAccount;

    /**
     * 合计金额 (不含税)
     */
    private BigDecimal totalAmount;

    /**
     * 合计税额
     */
    private BigDecimal totalTax;

    /**
     * 价税合计(大写)
     */
    private String totalPriceAndTaxInWords;

    /**
     * 价税合计(小写)
     */
    private BigDecimal totalPriceAndTaxInFigures;

    /**
     * 收款人
     */
    private String payee;

    /**
     * 复核人
     */
    private String reviewer;

    /**
     * 开票人
     */
    private String issuer;

    /**
     * 备注
     */
    private String remark; // Corresponds to BaseEntity's remark field

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 发票项目列表
     */
    private List<InvoiceItemResponseDTO> items;
}
