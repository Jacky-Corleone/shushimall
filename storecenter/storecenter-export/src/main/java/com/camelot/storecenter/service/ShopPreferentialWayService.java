package com.camelot.storecenter.service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.storecenter.dto.ShopPreferentialWayDTO;

import java.util.List;

/**
 * <p>Description: [优惠方式service]</p>
 * Created on 2015年10月26日
 *
 * @author <a href="mailto: guojianning@camelotchina.com">郭建宁</a>
 * @version 1.0
 *          Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface ShopPreferentialWayService {

    /**
     * <p>Discription:[优惠方式添加]</p>
     * Created on 2015年10月26日
     * @param dto
     * @return
     * @author:[郭建宁]
     */
    public ExecuteResult<String> addShopPreferentialWay(ShopPreferentialWayDTO dto);
    /**
     * <p>Discription:[优惠方式删除]</p>
     * Created on 2015年10月26日
     * @param dto
     * @return
     * @author:[郭建宁]
     */
    public ExecuteResult<String> deleteShopPreferentialWay(ShopPreferentialWayDTO dto);
    /**
     * <p>Discription:[优惠方式修改]</p>
     * Created on 2015年10月26日
     * @param dto
     * @return
     * @author:[郭建宁]
     */
    public ExecuteResult<String> updateShopPreferentialWay(ShopPreferentialWayDTO dto);
    /**
     * <p>Discription:[优惠方式添加]</p>
     * Created on 2015年10月26日
     * @param dto
     * @return
     * @author:[郭建宁]
     */
    public ExecuteResult<List<ShopPreferentialWayDTO>> queryShopPreferentialWay(ShopPreferentialWayDTO dto);
}
