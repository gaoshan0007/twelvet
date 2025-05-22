package com.twelvet.api.invoice.dto; // Corrected package

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for creating or updating an invoice item.
 *
 * @author TwelveT
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemRequestDTO {

    /**
     * 货物或应税劳务、服务名称
     */
    @NotBlank(message = "货物或应税劳务、服务名称不能为空 (Item name cannot be blank)")
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
    @NotNull(message = "数量不能为空 (Quantity cannot be null)")
    @DecimalMin(value = "0.0", inclusive = false, message = "数量必须大于0 (Quantity must be greater than 0)")
    @Digits(integer = 18, fraction = 6, message = "数量格式不正确 (Quantity format is invalid)")
    private BigDecimal quantity;

    /**
     * 单价
     */
    @NotNull(message = "单价不能为空 (Unit price cannot be null)")
    @DecimalMin(value = "0.0", inclusive = false, message = "单价必须大于0 (Unit price must be greater than 0)")
    @Digits(integer = 18, fraction = 6, message = "单价格式不正确 (Unit price format is invalid)")
    private BigDecimal unitPrice;

    /**
     * 金额 (不含税) = quantity * unitPrice. This can be calculated or validated.
     * For request, it's often better to calculate on the backend.
     * However, if provided, it can be validated against quantity and unitPrice.
     */
    @NotNull(message = "金额(不含税)不能为空 (Amount before tax cannot be null)")
    @DecimalMin(value = "0.0", inclusive = true, message = "金额(不含税)必须大于或等于0 (Amount before tax must be greater than or equal to 0)")
    @Digits(integer = 18, fraction = 2, message = "金额(不含税)格式不正确 (Amount before tax format is invalid)")
    private BigDecimal amountBeforeTax;

    /**
     * 税率 (e.g., 0.13 for 13%)
     */
    @NotNull(message = "税率不能为空 (Tax rate cannot be null)")
    @DecimalMin(value = "0.0", inclusive = true, message = "税率必须大于或等于0 (Tax rate must be greater than or equal to 0)")
    @Digits(integer = 3, fraction = 4, message = "税率格式不正确 (Tax rate format is invalid, e.g., 0.13)")
    private BigDecimal taxRate;

    /**
     * 税额 = amountBeforeTax * taxRate. This can be calculated or validated.
     * Similar to amountBeforeTax, often calculated on backend.
     */
    @NotNull(message = "税额不能为空 (Tax amount cannot be null)")
    @DecimalMin(value = "0.0", inclusive = true, message = "税额必须大于或等于0 (Tax amount must be greater than or equal to 0)")
    @Digits(integer = 18, fraction = 2, message = "税额格式不正确 (Tax amount format is invalid)")
    private BigDecimal taxAmount;

}
