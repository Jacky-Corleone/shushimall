package com.camelot.maketcenter.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.camelot.maketcenter.dao.PromotionDAO;
import com.camelot.maketcenter.dao.PromotionFullReductionDAO;
import com.camelot.maketcenter.dao.PromotionInfoDAO;
import com.camelot.maketcenter.dao.PromotionMarkdownDAO;
import com.camelot.maketcenter.dto.PromotionFrDTO;
import com.camelot.maketcenter.dto.PromotionFullReduction;
import com.camelot.maketcenter.dto.PromotionInDTO;
import com.camelot.maketcenter.dto.PromotionInfo;
import com.camelot.maketcenter.dto.PromotionInfoDTO;
import com.camelot.maketcenter.dto.PromotionMarkdown;
import com.camelot.maketcenter.dto.PromotionMdDTO;
import com.camelot.maketcenter.dto.PromotionOutDTO;
import com.camelot.maketcenter.dto.indto.PromotionFullReducitonInDTO;
import com.camelot.maketcenter.dto.indto.PromotionMarkdownInDTO;
import com.camelot.maketcenter.service.PromotionService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.DateCommon;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

@Service("promotionService")
public class PromotionServiceImpl implements  PromotionService{
    private final static Logger logger = LoggerFactory.getLogger(PromotionServiceImpl.class);
    @Resource
    private PromotionDAO promotionDAO;
    @Resource
    private PromotionInfoDAO promotionInfoDAO;
    @Resource
    private PromotionFullReductionDAO promotionFullReductionDAO;
    @Resource
    private PromotionMarkdownDAO promotionMarkdownDAO;
    @Override
    public ExecuteResult<DataGrid<PromotionOutDTO>> getPromotion(PromotionInDTO promotionInDTO,Pager page) {
        ExecuteResult<DataGrid<PromotionOutDTO>> result=new ExecuteResult<DataGrid<PromotionOutDTO>>();

        try {
            DataGrid<PromotionOutDTO> dataGrid=new DataGrid<PromotionOutDTO>();
            List<PromotionOutDTO> promotion=promotionDAO.getPromotion(promotionInDTO,page);
            Long count=promotionDAO.getPromotionCount(promotionInDTO);
            dataGrid.setRows(promotion);
            dataGrid.setTotal(count);
            result.setResult(dataGrid);
            result.setResultMessage("success");
        } catch (Exception e) {
            result.setResultMessage("error");
            result.getErrorMessages().add(e.getMessage());
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        return result;
    }
    @Override
    public ExecuteResult<String> modifyPromotionOnlineState(Integer onlineState, Long... promotionInfoId) {
        ExecuteResult<String> result=new ExecuteResult<String>();

        try {
            PromotionInfo promotionInfo=new PromotionInfo();
            Integer count=0;
            for (int i = 0; i < promotionInfoId.length; i++) {
                promotionInfo.setId(promotionInfoId[i]);
                promotionInfo.setOnlineState(onlineState);
                count+=promotionDAO.modifyPromotionOnlineState(promotionInfo);
            }

            result.setResult(count+"");
            result.setResultMessage("success");
        } catch (Exception e) {
            result.setResultMessage("error");
            result.getErrorMessages().add(e.getMessage());
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        return result;
    }
    @Override
    public ExecuteResult<String> addPromotionFullReduciton(PromotionFullReducitonInDTO promotionFullReducitonInDTO) {
        ExecuteResult<String> result=new ExecuteResult<String>();

        try {
            PromotionInfoDTO promotionInfoDTO = promotionFullReducitonInDTO.getPromotionInfoDTO();
            PromotionInfo promotionInfo = new PromotionInfo();
            BeanUtils.copyProperties(promotionInfoDTO, promotionInfo);
            Date startTime=promotionInfo.getStartTime();	//促销开始时间
            Date endTime=promotionInfo.getEndTime();	//促销结束时间

            if(endTime!=null && endTime.before(new Date())){
                result.setResultMessage("error");
                result.getErrorMessages().add("结束时间不能小于当前时间!");
                return result;
            }
            if(startTime!=null && endTime!=null && startTime.after(endTime)){
                result.setResultMessage("error");
                result.getErrorMessages().add("开始时间不能大于结束时间!");
                return result;
            }

            if(promotionInfo.getId()==null){
                promotionInfoDAO.add(promotionInfo);
            }
            for (PromotionFrDTO promotionFrDTO : promotionFullReducitonInDTO.getPromotionFrDTOList()) {
            	PromotionFullReduction promotionFullReduction = new PromotionFullReduction();
                BeanUtils.copyProperties(promotionFrDTO, promotionFullReduction);
                promotionFullReduction.setPromotionInfoId(promotionInfo.getId());
                promotionFullReductionDAO.add(promotionFullReduction);
            }
            result.setResult("");
            result.setResultMessage("success");
        } catch (Exception e) {
            result.setResultMessage("error");
            result.getErrorMessages().add(e.getMessage());
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        return result;
    }
    @Override
    public ExecuteResult<String> addPromotionMarkdown(PromotionMarkdownInDTO promotionMarkdownInDTO) {
        ExecuteResult<String> result=new ExecuteResult<String>();
        try {
        	PromotionInfoDTO promotionInfoDTO = promotionMarkdownInDTO.getPromotionInfoDTO();
        	PromotionInfo promotionInfo = new PromotionInfo();
            BeanUtils.copyProperties(promotionInfoDTO, promotionInfo);
            boolean flag=true;
            Date startTime=promotionInfo.getStartTime();	//促销开始时间
            Date endTime=promotionInfo.getEndTime();	//促销结束时间

            if(endTime!=null && endTime.before(new Date())){
                flag=false;
                result.setResultMessage("error");
                result.getErrorMessages().add("结束时间不能小于当前时间!");
            }
            if(startTime!=null && endTime!=null && startTime.after(endTime)){
                flag=false;
                result.setResultMessage("error");
                result.getErrorMessages().add("开始时间不能大于结束时间!");
            }
            if(flag==false){
            	return result;
            }

          //判断isAllItem字段是否为空
            if(promotionInfo.getIsAllItem()==null){
            	if(promotionMarkdownInDTO.getPromotionMdDTOList().isEmpty() || promotionMarkdownInDTO.getPromotionMdDTOList().get(0).getItemId()==null){
            		flag=false;
                    result.setResultMessage("error");
                    result.getErrorMessages().add("活动的折扣信息不能为空!");
            	}else{
            		if(promotionMarkdownInDTO.getPromotionMdDTOList().get(0).getItemId()==0){
	            		promotionInfo.setIsAllItem(1);
	            	}else{
	            		promotionInfo.setIsAllItem(2);
	            	}
            	}
            }
            
            if(flag==false){
            	return result;
            }
            
            List<Long> itemIdList=new ArrayList<Long>();
            PromotionInfo piQuery=new PromotionInfo();
            piQuery.setShopId(promotionInfo.getShopId());
            if(promotionInfo.getIsAllItem()!=null && promotionInfo.getIsAllItem()==1){
            	//查询店铺未结束的活动
            	 List<PromotionInfo> shopAllList = promotionInfoDAO.queryMdTimeFrameByItemId(piQuery);
                 for (PromotionInfo pi : shopAllList) {
         			//如果时间范围有交集，则插入失败
         			if(!DateCommon.isHaveNoIntersection(startTime, endTime, pi.getStartTime(), pi.getEndTime())){
         				//itemIdList.add(promotionMarkdown.getItemId());
         				result.addErrorMessage("时间范围与已有的范围有交集");
         				break;
         			}
         		}
            }else if(promotionInfo.getIsAllItem()!=null && promotionInfo.getIsAllItem()==2){
            	//查询店铺未结束的  针对所有商品的活动和 针对该商品的活动
            	for ( PromotionMdDTO promotionMdDTO : promotionMarkdownInDTO.getPromotionMdDTOList()) {
            		PromotionMarkdown promotionMarkdown = new PromotionMarkdown();
                    BeanUtils.copyProperties(promotionMdDTO, promotionMarkdown);
            		piQuery.setItemId(promotionMarkdown.getItemId());
            		//根据商品编号 查询所涉及到的折扣活动
            		List<PromotionInfo> list = promotionInfoDAO.queryMdTimeFrameByItemId(piQuery);
            		for (PromotionInfo pi : list) {
            			//如果时间范围有交集，则插入失败
            			if(!DateCommon.isHaveNoIntersection(startTime, endTime, pi.getStartTime(), pi.getEndTime())){
            				itemIdList.add(promotionMarkdown.getItemId());
            				result.addErrorMessage("时间范围与已有的范围有交集");
            				break;
            			}
            		}
            	}
            }
            if(result.isSuccess()){
                promotionInfoDAO.add(promotionInfo);
                for ( PromotionMdDTO promotionMdDTO : promotionMarkdownInDTO.getPromotionMdDTOList()) {
                    if(!itemIdList.contains(promotionMdDTO.getItemId())){
                    	promotionMdDTO.setPromotionInfoId(promotionInfo.getId());
                        PromotionMarkdown promotionMarkdown = new PromotionMarkdown();
                        BeanUtils.copyProperties(promotionMdDTO, promotionMarkdown);
                        promotionMarkdownDAO.add(promotionMarkdown);
                    }
                }
                result.setResult("");
                result.setResultMessage("success");
            }else{
                result.setResultMessage("error");
                result.setResult(itemIdList.toString().replace("[", "").replace("]", ""));
            }
        } catch (Exception e) {
            result.setResultMessage("error");
            result.getErrorMessages().add(e.getMessage());
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        return result;
    }
    @Override
    public ExecuteResult<String> deletePromotion(Long... promotionInfoId) {
        ExecuteResult<String> result=new ExecuteResult<String>();
        try {
            for (int i = 0; i < promotionInfoId.length; i++) {
                promotionInfoDAO.delete(promotionInfoId[i]);
//                promotionMarkdownDAO.deleteByPromotionId(promotionInfoId[i]);
//                promotionFullReductionDAO.deleteByPromotionId(promotionInfoId[i]);
            }
            result.setResult("");
            result.setResultMessage("success");
        } catch (Exception e) {
            result.setResultMessage("error");
            result.getErrorMessages().add(e.getMessage());
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return result;
    }
    @Override
    public ExecuteResult<String> editPromotionFullReduciton(
            PromotionFullReducitonInDTO promotionFullReducitonInDTO) {
        ExecuteResult<String> result=new ExecuteResult<String>();
        //促销活动id
        PromotionInfoDTO promotionInfoDTO = promotionFullReducitonInDTO.getPromotionInfoDTO();
        PromotionInfo promotionInfo = new PromotionInfo();
        BeanUtils.copyProperties(promotionInfoDTO, promotionInfo);

        Date startTime=promotionInfo.getStartTime();	//促销开始时间
        Date endTime=promotionInfo.getEndTime();	//促销结束时间

        if(endTime!=null && endTime.before(new Date())){
            result.setResultMessage("error");
            result.getErrorMessages().add("结束时间不能小于当前时间!");
            return result;
        }
        if(startTime!=null && endTime!=null && startTime.after(endTime)){
            result.setResultMessage("error");
            result.getErrorMessages().add("开始时间不能大于结束时间!");
            return result;
        }

        Long promotionInfoId=promotionInfo.getId();
        if(promotionInfoId==null){
            result.setResultMessage("error");
            result.getErrorMessages().add("促销活动id不能为空!");
            return result;
        }

        PromotionFullReduction promotionFullReduction=new PromotionFullReduction();
        promotionFullReduction.setPromotionInfoId(promotionInfoId);
        try {
            //促销活动所对应的满减列表信息
            List<PromotionFullReduction> queryList = promotionFullReductionDAO.queryList(promotionFullReduction, null);
            Map<Long,Long> map=new HashMap<Long,Long>(); //存放  <itemId,满减id>
            for (PromotionFullReduction pfQuery : queryList) {
                map.put(pfQuery.getItemId(),pfQuery.getId());
            }

            promotionInfoDAO.update(promotionInfo);
            for (PromotionFrDTO promotionFrDTO : promotionFullReducitonInDTO.getPromotionFrDTOList()) {
            	PromotionFullReduction pf = new PromotionFullReduction();
                BeanUtils.copyProperties(promotionFrDTO, pf);
                if(map.keySet().contains(pf.getItemId())){
                    pf.setId(map.get(pf.getItemId()));
                    promotionFullReductionDAO.update(pf);
                    map.keySet().remove(pf.getItemId());
                }else{
                    pf.setPromotionInfoId(promotionInfoId);
                    promotionFullReductionDAO.add(pf);
                }
            }

            //删除多余的满减信息
            Iterator<Entry<Long, Long>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<Long, Long> entry = iterator.next();
                PromotionFullReduction deletePO=new PromotionFullReduction();
                deletePO.setId(entry.getValue());//满减信息表的id
                promotionFullReductionDAO.delete(deletePO);
            }

            result.setResult("");
            result.setResultMessage("success");
        } catch (Exception e) {
            result.setResultMessage("error");
            result.getErrorMessages().add(e.getMessage());
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        return result;
    }
    @Override
    public ExecuteResult<String> editPromotionMarkdown(PromotionMarkdownInDTO promotionMarkdownInDTO) {
        ExecuteResult<String> result=new ExecuteResult<String>();
        try {
        	PromotionInfoDTO promotionInfoDTO = promotionMarkdownInDTO.getPromotionInfoDTO();
        	PromotionInfo promotionInfo = new PromotionInfo();
            BeanUtils.copyProperties(promotionInfoDTO, promotionInfo);
            boolean flag=true;
            Date startTime=promotionInfo.getStartTime();	//促销开始时间
            Date endTime=promotionInfo.getEndTime();	//促销结束时间

            if(startTime!=null && startTime.before(new Date())){
                flag=false;
                result.setResultMessage("error");
                result.getErrorMessages().add("开始时间不能小于当前时间!");
            }
            if(startTime!=null && endTime!=null && startTime.after(endTime)){
                flag=false;
                result.setResultMessage("error");
                result.getErrorMessages().add("开始时间不能大于结束时间!");
            }
            if(promotionInfo.getId()==null){
                flag=false;
                result.setResultMessage("error");
                result.getErrorMessages().add("促销活动id不能为空!");
            }
            
            //判断isAllItem字段是否为空
            if(promotionInfo.getIsAllItem()==null){
            	if(promotionMarkdownInDTO.getPromotionMdDTOList().isEmpty() || promotionMarkdownInDTO.getPromotionMdDTOList().get(0).getItemId()==null){
            		flag=false;
                    result.setResultMessage("error");
                    result.getErrorMessages().add("活动的折扣信息不能为空!");
            	}else{
            		if(promotionMarkdownInDTO.getPromotionMdDTOList().get(0).getItemId()==0){
	            		promotionInfo.setIsAllItem(1);
	            	}else{
	            		promotionInfo.setIsAllItem(2);
	            	}
            	}
            }
            
            if(flag==false){
            	return result;
            }
            
            List<Long> itemIdList=new ArrayList<Long>();
            
            PromotionInfo piQuery=new PromotionInfo();
            piQuery.setShopId(promotionInfo.getShopId());
            if(promotionInfo.getIsAllItem()!=null && promotionInfo.getIsAllItem()==1){
            	//查询店铺未结束的活动
            	 List<PromotionInfo> shopAllList = promotionInfoDAO.queryMdTimeFrameByItemId(piQuery);
                 for (PromotionInfo pi : shopAllList) {
                	 
                	 if(promotionInfo.getId().longValue()==pi.getId()){
                         continue;
                     }
                	 
         			//如果时间范围有交集，则插入失败
         			if(!DateCommon.isHaveNoIntersection(startTime, endTime, pi.getStartTime(), pi.getEndTime())){
         				//itemIdList.add(promotionMarkdown.getItemId());
         				result.addErrorMessage("时间范围与已有的范围有交集");
         				break;
         			}
         		}
            }else if(promotionInfo.getIsAllItem()!=null && promotionInfo.getIsAllItem()==2){
            	//查询店铺未结束的  针对所有商品的活动和 针对该商品的活动
            	for ( PromotionMdDTO promotionMdDTO : promotionMarkdownInDTO.getPromotionMdDTOList()) {
            		PromotionMarkdown promotionMarkdown = new PromotionMarkdown();
                    BeanUtils.copyProperties(promotionMdDTO, promotionMarkdown);
            		piQuery.setItemId(promotionMarkdown.getItemId());
            		//根据商品编号 查询所涉及到的折扣活动
            		List<PromotionInfo> list = promotionInfoDAO.queryMdTimeFrameByItemId(piQuery);
            		for (PromotionInfo pi : list) {
            			if(promotionInfo.getId().longValue()==pi.getId()){
                            continue;
                        }
            			
            			//如果时间范围有交集，则插入失败
            			if(!DateCommon.isHaveNoIntersection(startTime, endTime, pi.getStartTime(), pi.getEndTime())){
            				itemIdList.add(promotionMarkdown.getItemId());
            				result.addErrorMessage("时间范围与已有的范围有交集");
            				break;
            			}
            		}
            	}
            }

            if(result.isSuccess()){
                promotionInfoDAO.update(promotionInfo);
                promotionMarkdownDAO.deleteByPromotionId(promotionInfo.getId());

                for ( PromotionMdDTO promotionMdDTO : promotionMarkdownInDTO.getPromotionMdDTOList()) {
	                	PromotionMarkdown promotionMarkdown = new PromotionMarkdown();
	                    BeanUtils.copyProperties(promotionMdDTO, promotionMarkdown);
                	if(!itemIdList.contains(promotionMarkdown.getItemId())){
                        promotionMarkdown.setPromotionInfoId(promotionInfo.getId());
                        promotionMarkdownDAO.add(promotionMarkdown);
                    }
                }
                result.setResult("");
                result.setResultMessage("success");
            }else{
                result.setResultMessage("error");
                result.setResult(itemIdList.toString().replace("[", "").replace("]", ""));
            }
        } catch (Exception e) {
            result.setResultMessage("error");
            result.getErrorMessages().add(e.getMessage());
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        return result;
    }

    /**
     * 修改活动
     * @param promotionInfo
     * @return
     */
    public ExecuteResult<String> updatePromotion(PromotionInfo promotionInfo){
        ExecuteResult<String> result=new ExecuteResult<String>();
        try {
            Integer num=promotionInfoDAO.update(promotionInfo);
            if(num>0){
                result.setResultMessage("success");
            }else{
                result.setResultMessage("error");
            }
        } catch (Exception e) {
            result.setResultMessage("error");
            result.getErrorMessages().add(e.getMessage());
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return result;
    }
    @Override
    public ExecuteResult<List<Long>> getMdPromotionConflict(Long shopId,Date startTime,Date endTime){
    	
    	ExecuteResult<List<Long>> result = new ExecuteResult<List<Long>>();
    	List<Long> itemList = new LinkedList<Long>();
    	PromotionInfo promotionInfo = new PromotionInfo();
    	if(shopId != 0){
    		promotionInfo.setShopId(shopId);
    	}
    	//查询店铺未结束的活动
   	 	List<PromotionInfo> shopAllList = promotionInfoDAO.queryMdTimeFrameByItemId(promotionInfo);
        for (PromotionInfo pi : shopAllList) {
			//如果时间范围有交集，则插入失败
			if(!DateCommon.isHaveNoIntersection(startTime, endTime, pi.getStartTime(), pi.getEndTime())){
				//itemIdList.add(promotionMarkdown.getItemId());
				result.addErrorMessage("时间范围与已有的范围有交集");
				itemList.add(pi.getItemId());
			}
		}
    	result.setResult(itemList);
    	return result;
    }
}
