package com.twelvet.api.invoice.feign.factory;

import com.twelvet.api.invoice.dto.InvoiceRequestDTO;
import com.twelvet.api.invoice.dto.InvoiceResponseDTO;
import com.twelvet.api.invoice.feign.RemoteInvoiceService;
import com.twelvet.framework.core.application.page.TableDataInfo;
import com.twelvet.framework.core.domain.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * RemoteInvoiceService Fallback Factory.
 * Provides a fallback mechanism for the Feign client.
 *
 * @author TwelveT
 */
@Component
public class RemoteInvoiceFallbackFactory implements FallbackFactory<RemoteInvoiceService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteInvoiceFallbackFactory.class);

    @Override
    public RemoteInvoiceService create(Throwable cause) {
        log.error("Invoice service call fallback: {}", cause.getMessage(), cause);
        return new RemoteInvoiceService() {
            @Override
            public R<InvoiceResponseDTO> getInvoice(Long invoiceId, String source) {
                return R.fail("Failed to get invoice: " + cause.getMessage());
            }

            @Override
            public R<TableDataInfo<InvoiceResponseDTO>> listInvoices(InvoiceRequestDTO queryParams, String source) {
                return R.fail("Failed to list invoices: " + cause.getMessage());
            }

            @Override
            public R<Long> createInvoice(InvoiceRequestDTO invoiceDto, String source) {
                return R.fail("Failed to create invoice: " + cause.getMessage());
            }

            @Override
            public R<Void> updateInvoice(Long invoiceId, InvoiceRequestDTO invoiceDto, String source) {
                return R.fail("Failed to update invoice: " + cause.getMessage());
            }

            @Override
            public R<Void> deleteInvoice(Long invoiceId, String source) {
                return R.fail("Failed to delete invoice: " + cause.getMessage());
            }
        };
    }
}
