package com.demo.cameldemo.jdbc;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

//@Component
public class JDBC批量执行 extends RouteBuilder {


	@Autowired
	DefaultListableBeanFactory defaultListableBeanFactory;

	@Override
	public void configure() throws Exception {
		注册数据库("49", "jdbc:oracle:thin:@223.87.179.49:1521:ORCL");
//		注册数据库("183", "jdbc:oracle:thin:@119.27.161.183:49161:XE");
//		注册数据库("110", "jdbc:oracle:thin:@x23178478p.iok.la:1521:ORCL");

		批量执行效率();
//        非批量执行效率();
	}


	public void 非批量执行效率() {
		from("timer://非批量执行效率?repeatCount=1")
				.onCompletion().process(exchange -> {
			System.out.println(String.format("同步结束,数据量:%s,耗时:%dms", exchange.getIn().getHeader("CamelJdbcRowCount"), System.currentTimeMillis() - (long) exchange.getProperty("CostTime")));
		})
				.end()
				.process(ex -> {
					ex.setProperty("CostTime", System.currentTimeMillis());
					System.out.println("###### 开始同步!" + ex.getExchangeId());
				})
				.setBody(constant("select * from SYS_USER"))
				.to("jdbc:49")
				.process(exchange -> {
					System.out.println(String.format("查询结束,数据量:%s,耗时:%dms", exchange.getIn().getHeader("CamelJdbcRowCount"), System.currentTimeMillis() - (long) exchange.getProperty("CostTime")));
				})
//				.split(body())
				.setBody(simple("INSERT INTO SYS_USER_COPY(ID, USERNAME, PASSWORD, CONTENT, INFO, DD, TT) VALUES ('${body[ID]}','${body[USERNAME]}','${body[PASSWORD]}','${body[CONTENT]}','${body[INFO]}','${body[DD]}','${body[TT]}')"))
				.to("jdbc:49")
		;

	}

	public void 批量执行效率() {

		from("timer://批量执行效率?repeatCount=1")
				.process(ex -> {
					ex.setProperty("CostTime", System.currentTimeMillis());
					System.out.println("###### 开始同步!" + ex.getExchangeId());
				})
				.to("sql:DELETE FROM T_OA_MEETING_LIST where 1=1?dataSource=49&batch=true&transacted=true")
				.process(exchange -> {
					System.out.println(String.format("查询结束,数据量:%s,耗时:%dms", exchange.getIn().getHeader("CamelSqlRowCount"), System.currentTimeMillis() - (long) exchange.getProperty("CostTime")));
				})
//				.to("sql:DELETE FROM SYS_USER_COPY WHERE ID=:#ID?dataSource=183&batch=true&transacted=true") // 数据存在更新时的场景,先删除再新增
//				.to("sql://INSERT%20INTO%20SYS_USER_COPY(ID,USERNAME,PASSWORD,CONTENT,INFO,DD,TT)%20values%20(:#ID,:#USERNAME,:#PASSWORD,:#CONTENT,:#INFO,:#DD,:#TT)?batch=true&dataSource=49")
		;


		// TODO: 2019-03-09 多sql事务无法共享
		from("timer://批量执行效率?repeatCount=1")
				.onCompletion().process(exchange -> {
			System.out.println(String.format("同步结束,数据量:%s,耗时:%dms", exchange.getIn().getHeader("CamelSqlRowCount"), System.currentTimeMillis() - (long) exchange.getProperty("CostTime")));
		})
				.end()
				.process(ex -> {
					ex.setProperty("CostTime", System.currentTimeMillis());
					System.out.println("###### 开始同步!" + ex.getExchangeId());
				})
				.to("sql://SELECT%20%20SYS_USER.ID%20,%20SYS_USER.USERNAME%20,%20SYS_USER.PASSWORD%20,%20SYS_USER.CONTENT%20,%20SYS_USER.INFO%20,%20SYS_USER.DD%20,%20SYS_USER.TT%20FROM%20SYS_USER%20where%201=1?dataSource=49")
				.process(exchange -> {
					System.out.println(String.format("查询结束,数据量:%s,耗时:%dms", exchange.getIn().getHeader("CamelSqlRowCount"), System.currentTimeMillis() - (long) exchange.getProperty("CostTime")));
				})
//				.to("sql:DELETE FROM SYS_USER_COPY WHERE ID=:#ID?dataSource=183&batch=true&transacted=true") // 数据存在更新时的场景,先删除再新增
				.to("sql://INSERT%20INTO%20SYS_USER_COPY(ID,USERNAME,PASSWORD,CONTENT,INFO,DD,TT)%20values%20(:#ID,:#USERNAME,:#PASSWORD,:#CONTENT,:#INFO,:#DD,:#TT)?batch=true&dataSource=49")
		;
	}

	void 注册数据库(String name, String url) {
		BeanDefinitionBuilder builder;
		builder = BeanDefinitionBuilder.genericBeanDefinition(
				org.apache.commons.dbcp.BasicDataSource.class);
		builder.addPropertyValue("initialSize", 5);
		builder.addPropertyValue("maxActive", 10);
		builder.addPropertyValue("maxIdle", 100);
		builder.addPropertyValue("minIdle", 20);
		builder.addPropertyValue("maxWait", 9999);


		builder.addPropertyValue("driverClassName", "oracle.jdbc.driver.OracleDriver");
		builder.addPropertyValue("url", url);
		builder.addPropertyValue("username", "i3s");
		builder.addPropertyValue("password", "i3s");
		defaultListableBeanFactory.registerBeanDefinition(name, builder.getBeanDefinition());

	}
}
