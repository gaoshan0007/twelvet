package com.twelvet.api.invoice.dto; // Corrected package

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO for creating or updating an invoice.
 *
 * @author TwelveT
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceRequestDTO {

    /**
     * 发票代码 (fāpiào dàimǎ)
     */
    @NotBlank(message = "发票代码不能为空 (Invoice code cannot be blank)")
    @Size(max = 50, message = "发票代码长度不能超过50个字符 (Invoice code length cannot exceed 50 characters)")
    private String invoiceCode;

    /**
     * 发票号码 (fāpiào hàomǎ)
     */
    @NotBlank(message = "发票号码不能为空 (Invoice number cannot be blank)")
    @Size(max = 50, message = "发票号码长度不能超过50个字符 (Invoice number length cannot exceed 50 characters)")
    private String invoiceNumber;

    /**
     * 开票日期 (kāipiào rìqī)
     */
    @NotNull(message = "开票日期不能为空 (Invoice date cannot be null)")
    private LocalDate invoiceDate;

    /**
     * 校验码 (jiàoyàn mǎ) - Usually last 6 digits for certain electronic invoices
     */
    @Size(max = 20, message = "校验码长度不能超过20个字符 (Check code length cannot exceed 20 characters)")
    private String checkCode;

    /**
     * 机器编号 (jīqì biānhào) - Optional
     */
    @Size(max = 50, message = "机器编号长度不能超过50个字符 (Machine number length cannot exceed 50 characters)")
    private String machineNumber;

    /**
     * 购买方名称
     */
    @NotBlank(message = "购买方名称不能为空 (Purchaser name cannot be blank)")
    @Size(max = 200, message = "购买方名称长度不能超过200个字符 (Purchaser name length cannot exceed 200 characters)")
    private String purchaserName;

    /**
     * 购买方纳税人识别号
     */
    @NotBlank(message = "购买方纳税人识别号不能为空 (Purchaser TIN cannot be blank)")
    @Size(max = 50, message = "购买方纳税人识别号长度不能超过50个字符 (Purchaser TIN length cannot exceed 50 characters)")
    private String purchaserTin;

    /**
     * 购买方地址、电话
     */
    @Size(max = 255, message = "购买方地址、电话长度不能超过255个字符 (Purchaser address/phone length cannot exceed 255 characters)")
    private String purchaserAddressPhone;

    /**
     * 购买方开户行及账号 (purchaserBankAccount was mispelled in prompt, correcting here)
     */
    @Size(max = 255, message = "购买方开户行及账号长度不能超过255个字符 (Purchaser bank account length cannot exceed 255 characters)")
    private String purchaserBankAccount;

    /**
     * 销售方名称
     */
    @NotBlank(message = "销售方名称不能为空 (Seller name cannot be blank)")
    @Size(max = 200, message = "销售方名称长度不能超过200个字符 (Seller name length cannot exceed 200 characters)")
    private String sellerName;

    /**
     * 销售方纳税人识别号
     */
    @NotBlank(message = "销售方纳税人识别号不能为空 (Seller TIN cannot be blank)")
    @Size(max = 50, message = "销售方纳税人识别号长度不能超过50个字符 (Seller TIN length cannot exceed 50 characters)")
    private String sellerTin;

    /**
     * 销售方地址、电话
     */
    @Size(max = 255, message = "销售方地址、电话长度不能超过255个字符 (Seller address/phone length cannot exceed 255 characters)")
    private String sellerAddressPhone;

    /**
     * 销售方开户行及账号
     */
    @Size(max = 255, message = "销售方开户行及账号长度不能超过255个字符 (Seller bank account length cannot exceed 255 characters)")
    private String sellerBankAccount;

    /**
     * 合计金额 (不含税) - Will be calculated from items on the backend
     */
    @NotNull(message = "合计金额(不含税)不能为空 (Total amount cannot be null)")
    @DecimalMin(value = "0.0", inclusive = true, message = "合计金额(不含税)必须大于或等于0")
    @Digits(integer = 18, fraction = 2, message = "合计金额(不含税)格式不正确")
    private BigDecimal totalAmount;

    /**
     * 合计税额 - Will be calculated from items on the backend
     */
    @NotNull(message = "合计税额不能为空 (Total tax cannot be null)")
    @DecimalMin(value = "0.0", inclusive = true, message = "合计税额必须大于或等于0")
    @Digits(integer = 18, fraction = 2, message = "合计税额格式不正确")
    private BigDecimal totalTax;

    /**
     * 价税合计(大写)
     */
    @NotBlank(message = "价税合计(大写)不能为空 (Total price and tax in words cannot be blank)")
    @Size(max = 255, message = "价税合计(大写)长度不能超过255个字符")
    private String totalPriceAndTaxInWords;

    /**
     * 价税合计(小写) - Will be calculated from items on the backend
     */
    @NotNull(message = "价税合计(小写)不能为空 (Total price and tax in figures cannot be null)")
    @DecimalMin(value = "0.0", inclusive = true, message = "价税合计(小写)必须大于或等于0")
    @Digits(integer = 18, fraction = 2, message = "价税合计(小写)格式不正确")
    private BigDecimal totalPriceAndTaxInFigures;

    /**
     * 收款人
     */
    @Size(max = 100, message = "收款人长度不能超过100个字符 (Payee length cannot exceed 100 characters)")
    private String payee;

    /**
     * 复核人
     */
    @Size(max = 100, message = "复核人长度不能超过100个字符 (Reviewer length cannot exceed 100 characters)")
    private String reviewer;

    /**
     * 开票人
     */
    @NotBlank(message = "开票人不能为空 (Issuer cannot be blank)")
    @Size(max = 100, message = "开票人长度不能超过100个字符 (Issuer length cannot exceed 100 characters)")
    private String issuer;

    /**
     * 备注 (optional)
     */
    private String remark; // This corresponds to BaseEntity's remark.

    /**
     * 发票项目列表
     */
    @Valid // Enable validation for nested DTOs
    @NotNull(message = "发票项目列表不能为空 (Invoice items cannot be null)")
    @Size(min = 1, message = "发票项目列表至少要包含一个项目 (Invoice items must contain at least one item)")
    private List<InvoiceItemRequestDTO> items;
}
