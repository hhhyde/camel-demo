package com.demo.cameldemo.webservices.cxf.service.impl;

import com.demo.cameldemo.webservices.cxf.service.BaseicQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author CYX
 * @create 2018-12-27-20:00
 */
public class BaseicQueryImpl implements BaseicQuery {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseicQueryImpl.class);

    @Override
    public String queryPeopleInfo(String queryCondition) {

        LOGGER.info("queryCondition : " + queryCondition);

        return "copy that";
    }
}
