package com.thinkgem.jeesite.modules.cms.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.cms.dao.RankingListDao;
import com.thinkgem.jeesite.modules.cms.entity.RankingList;

@Service
@Transactional(readOnly = true)
public class RankingListService extends BaseService{
	
	@Autowired
	private RankingListDao rankingListDao;
	
	public RankingList get(String id) {
		return rankingListDao.get(id);
	}
	
	public Page<RankingList> find(Page<RankingList> page, RankingList rankingList) {
		DetachedCriteria dc = rankingListDao.createDetachedCriteria();
		dc.add(Restrictions.eq("delFlag", rankingList.DEL_FLAG_NORMAL));
		if(null!=rankingList.getKeywords() && !"".equals(rankingList.getKeywords())){
			dc.add(Restrictions.like("keywords", rankingList.getKeywords()));
		}
		dc.addOrder(Order.asc("sort"));
		return rankingListDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public boolean save(RankingList rankingList) {
		boolean flag = false;
		if(null!=rankingList && !"".equals(rankingList)){
			rankingListDao.save(rankingList);
			flag = true;
		}
		return flag;
	}
	
	@Transactional(readOnly = false)
	public boolean delete(String id) {
		boolean flag = false;
		if(null!=id && !"".equals(id)){
			rankingListDao.deleteById(id);
			flag = true;
		}
		return flag;
	}
	
	public List<RankingList> getRankingList (RankingList rankingList){
		List<RankingList> list = new ArrayList<RankingList>();
		DetachedCriteria dc = rankingListDao.createDetachedCriteria();
		dc.add(Restrictions.eq("delFlag", rankingList.DEL_FLAG_NORMAL));
		list=rankingListDao.find(dc);
		return list;
		
	}

}
