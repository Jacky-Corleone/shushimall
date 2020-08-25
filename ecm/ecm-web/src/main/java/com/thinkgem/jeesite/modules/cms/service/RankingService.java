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
import com.thinkgem.jeesite.modules.cms.dao.RankingDao;
import com.thinkgem.jeesite.modules.cms.dao.RankingListDao;
import com.thinkgem.jeesite.modules.cms.entity.Ranking;
import com.thinkgem.jeesite.modules.cms.entity.RankingList;

/**
 * 排行榜子信息列表
 * @author admin
 *
 */
@Service
@Transactional(readOnly = true)
public class RankingService extends BaseService{

	@Autowired
	private RankingListDao rankingListDao;
	
	@Autowired
	private RankingDao rankingDao;
	
	public Ranking get(String id) {
		return rankingDao.get(id);
	}
	
	public Page<Ranking> find(Page<Ranking> page, Ranking ranking) {
		DetachedCriteria dc = rankingDao.createDetachedCriteria();
		dc.add(Restrictions.eq("delFlag", ranking.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("sort"));
		return rankingDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public boolean save(Ranking ranking) {
		boolean flag = false;
		if(null!=ranking && !"".equals(ranking)){
			rankingDao.save(ranking);
			flag = true;
		}
		return flag;
	}
	
	@Transactional(readOnly = false)
	public boolean delete(String id) {
		boolean flag = false;
		if(null!=id && !"".equals(id)){
			rankingDao.deleteById(id);
			flag = true;
		}
		return flag;
	}

	public List<Ranking> findRankingList(String id, int type) {
		List<Ranking> list = new ArrayList<Ranking>();
		DetachedCriteria dc = rankingDao.createDetachedCriteria();
		dc.add(Restrictions.eq("delFlag", "0"));
		dc.add(Restrictions.eq("cid", id));
		dc.add(Restrictions.eq("type",type));
		list=rankingDao.find(dc);
		return list;
	}
}
