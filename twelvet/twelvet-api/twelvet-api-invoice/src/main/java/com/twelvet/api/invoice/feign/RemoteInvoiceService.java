package com.twelvet.api.invoice.feign;

import com.twelvet.api.invoice.dto.InvoiceRequestDTO;
import com.twelvet.api.invoice.dto.InvoiceResponseDTO;
import com.twelvet.api.invoice.feign.factory.RemoteInvoiceFallbackFactory;
import com.twelvet.framework.core.application.page.TableDataInfo;
import com.twelvet.framework.core.constants.SecurityConstants;
import com.twelvet.framework.core.constants.ServiceNameConstants;
import com.twelvet.framework.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Remote Invoice Service Feign client.
 *
 * @author TwelveT
 */
@FeignClient(contextId = "remoteInvoiceService",
             value = ServiceNameConstants.INVOICE_SERVICE, // This should be "twelvet-server-invoice"
             fallbackFactory = RemoteInvoiceFallbackFactory.class)
public interface RemoteInvoiceService {

    /**
     * Get Invoice by ID.
     *
     * @param invoiceId The ID of the invoice to retrieve.
     * @param source    The source of the request (internal call marker).
     * @return R containing the invoice details.
     */
    @GetMapping("/invoices/{invoiceId}")
    R<InvoiceResponseDTO> getInvoice(@PathVariable("invoiceId") Long invoiceId,
                                     @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * List Invoices with optional filtering and pagination.
     * Note: In controller, this was a GET with DTO as model attribute.
     * For Feign, if complex DTO, POST is often preferred.
     * If GET is strict, individual @RequestParam for each field or a Map is an option.
     * Assuming POST for Feign list operations with complex DTOs is acceptable.
     * If not, this needs to be a GET with @SpringQueryMap or individual params.
     *
     * @param queryParams DTO containing query parameters.
     * @param source      The source of the request.
     * @return R containing the paginated list of invoices.
     */
    @PostMapping("/invoices/list") // Changed to POST for Feign with complex DTO
    R<TableDataInfo<InvoiceResponseDTO>> listInvoices(@RequestBody InvoiceRequestDTO queryParams,
                                                      @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
    /*
    Alternative for GET if queryParams is simple enough or PageUtil is used to extract page params:
    @GetMapping("/invoices")
    R<TableDataInfo<InvoiceResponseDTO>> listInvoices(@SpringQueryMap InvoiceRequestDTO queryParams, // Requires OpenFeign 3.x+
                                                      @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
    */


    /**
     * Create a new Invoice.
     *
     * @param invoiceDto DTO containing the details of the invoice to create.
     * @param source     The source of the request.
     * @return R containing the ID of the created invoice.
     */
    @PostMapping("/invoices")
    R<Long> createInvoice(@Validated @RequestBody InvoiceRequestDTO invoiceDto,
                          @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * Update an existing Invoice.
     *
     * @param invoiceId  The ID of the invoice to update.
     * @param invoiceDto DTO containing the updated details.
     * @param source     The source of the request.
     * @return R indicating success or failure.
     */
    @PutMapping("/invoices/{invoiceId}")
    R<Void> updateInvoice(@PathVariable("invoiceId") Long invoiceId,
                          @Validated @RequestBody InvoiceRequestDTO invoiceDto,
                          @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * Delete an Invoice.
     *
     * @param invoiceId The ID of the invoice to delete.
     * @param source    The source of the request.
     * @return R indicating success or failure.
     */
    @DeleteMapping("/invoices/{invoiceId}")
    R<Void> deleteInvoice(@PathVariable("invoiceId") Long invoiceId,
                          @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
