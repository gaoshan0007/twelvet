package com.twelvet.framework.core.constants;

/**
 * @author twelvet
 * @WebSite twelvet.cn
 * @Description: 服务名称
 */
public interface ServiceNameConstants {

	/**
	 * 认证服务
	 */
	String AUTH_SERVICE = "twelvet-auth";

	/**
	 * 系统服务
	 */
	String SYSTEM_SERVICE = "twelvet-server-system";

	/**
	 * 文件服务的serviceId
	 */
	String FILE_SERVICE = "twelvet-server-dfs";

	/**
	 * AI服务的serviceId
	 */
	String AI_SERVICE = "twelvet-server-ai";

	/**
	 * 发票服务的serviceId
	 */
	String INVOICE_SERVICE = "twelvet-server-invoice"; // Ensure this matches the actual service name used for discovery

}
