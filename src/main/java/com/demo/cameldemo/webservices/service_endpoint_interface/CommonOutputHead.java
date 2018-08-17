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
        *@Title:
        *@Description:webService入口数据类型
        *@Author:Rose
        *@Since:2018年5月26日
        *@Version:1.1.0
        */
@Data
public class CommonOutputHead {

    		/**
            *系统接入ID
            */
        
    private String systemID;
    		/**
            *密钥
            */
        
    private String key;
    		/**
            *请求时间
            */
        
    private String transDate;
    
    		/**
            *
            */
    private String serviceID;
    		/**
            *调用第三方时存入是传入地址
            */
    private String URL;
    
    		/**
            *交易状态
            */
        
    private String tranStatus;
    
    		/**
            *交易描述
            */
        
    private String tranDesc;

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

			public String getTranStatus() {
				return tranStatus;
			}

			public void setTranStatus(String tranStatus) {
				this.tranStatus = tranStatus;
			}

			public String getTranDesc() {
				return tranDesc;
			}

			public void setTranDesc(String tranDesc) {
				this.tranDesc = tranDesc;
			}
    

   
}
