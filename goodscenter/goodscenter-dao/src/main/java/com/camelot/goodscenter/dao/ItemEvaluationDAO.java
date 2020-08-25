package com.camelot.goodscenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.goodscenter.domain.ItemBrand;
import com.camelot.goodscenter.domain.ItemEvaluation;
import com.camelot.goodscenter.dto.BrandOfShopDTO;
import com.camelot.goodscenter.dto.ItemBrandDTO;
import com.camelot.goodscenter.dto.ItemEvaluationDTO;
import com.camelot.goodscenter.dto.ItemEvaluationQueryInDTO;
import com.camelot.goodscenter.dto.ItemEvaluationQueryOutDTO;
import com.camelot.goodscenter.dto.ItemEvaluationReplyDTO;
import com.camelot.goodscenter.dto.ItemEvaluationShowDTO;
import com.camelot.goodscenter.dto.ItemEvaluationTotalQueryDTO;
import com.camelot.goodscenter.dto.ItemQueryInDTO;
import com.camelot.goodscenter.dto.ItemQueryOutDTO;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;

/**
 * <p>Description: [商品评价DAO]</p>
 * Created on 2015年2月3日
 * @author  <a href="mailto: zhangzhiqiang34@camelotchina.com">张志强</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface ItemEvaluationDAO extends BaseDAO<ItemEvaluation>{
	/**
	 * 
	 * <p>Discription:[添加商品评价]</p>
	 * Created on 2015-4-10
	 * @param ItemEvaluationDTO
	 * @author:zhangzq
	 */
	void insertItemEvaluation(ItemEvaluationDTO itemEvaluationDTO);
	
	/**
	 * 
	 * <p>Discription:[添加商品评价回复]</p>
	 * Created on 2015-4-10
	 * @param ItemEvaluationReplyDTO
	 * @author:zhangzq
	 */
	void insertItemEvaluationReply(ItemEvaluationReplyDTO itemEvaluationReplyDTO);
	
	/**
	 * 
	 * <p>Discription:[查询评价记录]</p>
	 * Created on 2015-4-10
	 * @param ItemEvaluationQueryInDTO
	 * @author:zhangzq
	 */
	List<ItemEvaluationQueryOutDTO> queryItemEvaluationList(@Param("entity") ItemEvaluationQueryInDTO entity, @Param("page") Pager page);
	
	
	/**
	 * 
	 * <p>Discription:[查询总条数]</p>
	 * Created on 2015-4-10
	 * @param ItemEvaluationQueryInDTO
	 * @author:zhangzq
	 */
	Long queryCount(@Param("entity") ItemEvaluationQueryInDTO entity);
	
	/**
	 * 
	 * <p>Discription:[统计 对店铺/对买家 的评价]</p>
	 * Created on 2015-4-10
	 * @param ItemEvaluationQueryInDTO
	 * @author:zhangzq
	 */
	List<ItemEvaluationTotalQueryDTO> queryItemEvaluationTotal(@Param("entity") ItemEvaluationQueryInDTO entity);
	
   /**
    * 查询卖家回复集合
    * @param itemEvaluationReplyDTO
    * @param page
    * @return
    */

   List<ItemEvaluationReplyDTO> queryItemEvaluationReplyList(@Param("itemEvaluationReplyDTO")ItemEvaluationReplyDTO itemEvaluationReplyDTO,@Param("page")Pager page);

   /**
    * 查询卖家回复总数
    * @param itemEvaluationReplyDTO
    * @return
    */
   Long queryTotal(@Param("itemEvaluationReplyDTO")ItemEvaluationReplyDTO itemEvaluationReplyDTO);

   /**
    * <p>查询item或sku的评价总数</p>
    * Created on 2016年2月29日
    * @param entity
    * @return
    * @author: 顾雨
    */
   int queryEvalCount(@Param("entity") ItemEvaluationQueryInDTO entity);
   
   /**
    * <p>查询item或sku的评价总数（有图）</p>
    * Created on 2016年2月29日
    * @param entity
    * @return
    * @author: 顾雨
    */
   int queryEvalShowCount(@Param("entity") ItemEvaluationQueryInDTO entity);
   
   /**
    * <p>查询item或sku的平均评价星级</p>
    * Created on 2016年2月29日
    * @param entity
    * @return
    * @author: 顾雨
    */
	Float queryAvgSkuScope(@Param("entity") ItemEvaluationQueryInDTO entity);

	/**
	 * <p>评价置为有图评价</p>
	 * Created on 2016年2月29日
	 * @param itemEvaluationShowDTO
	 * @author: 顾雨
	 */
	void setHasShow(ItemEvaluationShowDTO itemEvaluationShowDTO);

}
