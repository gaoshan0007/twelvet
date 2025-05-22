package com.twelvet.server.invoice.domain;

import com.twelvet.framework.core.application.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 发票主信息实体
 * Represents the main information of a Chinese VAT invoice.
 *
 * @author TwelveT
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "invoice")
public class Invoice extends BaseEntity {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id")
    private Long id;

    /**
     * 发票代码 (fāpiào dàimǎ)
     */
    @Column(name = "invoice_code", nullable = false, length = 50)
    private String invoiceCode;

    /**
     * 发票号码 (fāpiào hàomǎ)
     */
    @Column(name = "invoice_number", nullable = false, length = 50)
    private String invoiceNumber;

    /**
     * 开票日期 (kāipiào rìqī)
     */
    @Column(name = "invoice_date", nullable = false)
    private LocalDate invoiceDate;

    /**
     * 校验码 (jiàoyàn mǎ) - Usually last 6 digits for certain electronic invoices
     */
    @Column(name = "check_code", length = 20)
    private String checkCode;

    /**
     * 机器编号 (jīqì biānhào) - Optional
     */
    @Column(name = "machine_number", length = 50)
    private String machineNumber;

    /**
     * 购买方名称
     */
    @Column(name = "purchaser_name", nullable = false, length = 200)
    private String purchaserName;

    /**
     * 购买方纳税人识别号
     */
    @Column(name = "purchaser_tin", nullable = false, length = 50)
    private String purchaserTin;

    /**
     * 购买方地址、电话
     */
    @Column(name = "purchaser_address_phone", length = 255)
    private String purchaserAddressPhone;

    /**
     * 购买方开户行及账号
     */
    @Column(name = "purchaser_bank_account", length = 255)
    private String purchaserBankAccount;

    /**
     * 销售方名称
     */
    @Column(name = "seller_name", nullable = false, length = 200)
    private String sellerName;

    /**
     * 销售方纳税人识别号
     */
    @Column(name = "seller_tin", nullable = false, length = 50)
    private String sellerTin;

    /**
     * 销售方地址、电话
     */
    @Column(name = "seller_address_phone", length = 255)
    private String sellerAddressPhone;

    /**
     * 销售方开户行及账号
     */
    @Column(name = "seller_bank_account", length = 255)
    private String sellerBankAccount;

    /**
     * 合计金额 (不含税)
     */
    @Digits(integer = 18, fraction = 2)
    @Column(name = "total_amount", nullable = false, precision = 20, scale = 2)
    private BigDecimal totalAmount;

    /**
     * 合计税额
     */
    @Digits(integer = 18, fraction = 2)
    @Column(name = "total_tax", nullable = false, precision = 20, scale = 2)
    private BigDecimal totalTax;

    /**
     * 价税合计(大写)
     */
    @Column(name = "total_price_and_tax_in_words", nullable = false, length = 255)
    private String totalPriceAndTaxInWords;

    /**
     * 价税合计(小写)
     */
    @Digits(integer = 18, fraction = 2)
    @Column(name = "total_price_and_tax_in_figures", nullable = false, precision = 20, scale = 2)
    private BigDecimal totalPriceAndTaxInFigures;

    /**
     * 收款人
     */
    @Column(name = "payee", length = 100)
    private String payee;

    /**
     * 复核人
     */
    @Column(name = "reviewer", length = 100)
    private String reviewer;

    /**
     * 开票人
     */
    @Column(name = "issuer", nullable = false, length = 100)
    private String issuer;

    /**
     * 发票项目列表
     */
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<InvoiceItem> items;

    // createTime, updateTime, createBy, updateBy, remark are inherited from BaseEntity
}
