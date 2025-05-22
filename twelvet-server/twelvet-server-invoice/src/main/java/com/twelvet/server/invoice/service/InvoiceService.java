package com.twelvet.server.invoice.service;

import com.twelvet.api.invoice.dto.InvoiceRequestDTO; // Updated import
import com.twelvet.api.invoice.dto.InvoiceResponseDTO; // Updated import

import java.util.List;

/**
 * 发票服务接口
 *
 * @author TwelveT
 */
public interface InvoiceService {

    /**
     * 根据ID获取发票详情
     *
     * @param invoiceId 发票ID
     * @return 发票详情DTO
     */
    InvoiceResponseDTO getInvoiceById(Long invoiceId);

    /**
     * 查询发票列表
     * (Further details on pagination and filtering to be defined)
     *
     * @param queryParams 查询参数
     * @return 发票列表
     */
    List<InvoiceResponseDTO> listInvoices(InvoiceRequestDTO queryParams);

    /**
     * 创建发票
     *
     * @param invoiceDto 发票创建请求DTO
     * @return 创建的发票ID
     */
    Long createInvoice(InvoiceRequestDTO invoiceDto);

    /**
     * 更新发票
     *
     * @param invoiceId  发票ID
     * @param invoiceDto 发票更新请求DTO
     */
    void updateInvoice(Long invoiceId, InvoiceRequestDTO invoiceDto);

    /**
     * 删除发票
     *
     * @param invoiceId 发票ID
     */
    void deleteInvoice(Long invoiceId);

}
