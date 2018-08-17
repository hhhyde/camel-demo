/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.demo.cameldemo.webservices.service_endpoint_interface;

import lombok.Data;

/**
 * @Title:
 * @Description:webService入口数据类型
 * @Author:Rose
 * @Since:2018年5月26日
 * @Version:1.1.0
 */
@Data
public class CommonInputHead {

	/**
	 * 系统接入ID
	 */
	private String systemID;
	
			/**
	        *接入系统IP
	        */
	    
	private String systemIP;
	/**
	 * 密钥
	 */

	private String key;
	/**
	 * 请求时间
	 */

	private String transDate;

	/**
	*
	*/
	private String serviceID;
	/**
	 * 调用第三方时存入是传入地址
	 */
	private String URL;

	/**
	 * 备用字段1
	 */
	private String backCol1;
	/**
	 * 备用字段2
	 */
	private String backCol2;
	/**
	 * 备用字段3
	 */
	private String backCol3;
	public String getSystemID() {
		return systemID;
	}
	public void setSystemID(String systemID) {
		this.systemID = systemID;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getTransDate() {
		return transDate;
	}
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	public String getServiceID() {
		return serviceID;
	}
	public void setServiceID(String serviceID) {
		this.serviceID = serviceID;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	public String getBackCol1() {
		return backCol1;
	}
	public void setBackCol1(String backCol1) {
		this.backCol1 = backCol1;
	}
	public String getBackCol2() {
		return backCol2;
	}
	public void setBackCol2(String backCol2) {
		this.backCol2 = backCol2;
	}
	public String getBackCol3() {
		return backCol3;
	}
	public void setBackCol3(String backCol3) {
		this.backCol3 = backCol3;
	}
	public String getSystemIP() {
		return systemIP;
	}
	public void setSystemIP(String systemIP) {
		this.systemIP = systemIP;
	}

}
