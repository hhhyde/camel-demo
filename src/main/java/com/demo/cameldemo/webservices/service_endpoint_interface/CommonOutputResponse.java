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
public class CommonOutputResponse {

	private CommonOutputHead head;
	private CommonOutputBody body;

	public CommonOutputHead getHead() {
		return head;
	}
	public void setHead(CommonOutputHead head) {
		this.head = head;
	}
	public CommonOutputBody getBody() {
		return body;
	}
	public void setBody(CommonOutputBody body) {
		this.body = body;
	}
	
	public CommonOutputResponse(){
		this.head = new CommonOutputHead();
		this.body = new CommonOutputBody();
	}
}
