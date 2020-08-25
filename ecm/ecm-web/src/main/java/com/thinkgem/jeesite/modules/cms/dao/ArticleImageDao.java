/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.cms.dao;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.cms.entity.Article;
import com.thinkgem.jeesite.modules.cms.entity.ArticleImage;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文章DAO接口
 * @author ThinkGem
 * @version 2013-8-23
 */
@Repository
public class ArticleImageDao extends BaseDao<ArticleImage> {

    @Override
    public void save(ArticleImage entity) {
        getSession().saveOrUpdate(entity);
    }
    public List<ArticleImage> findByIdIn(String id){
        return find("from ArticleImage where aid in (:p1)",new Parameter(id));
    }
    public void deleteById(String id) {
        String hql = "delete from cms_article_img where id=" +"'"+id+"'";
        this.getSession().createSQLQuery(hql).executeUpdate();
        this.getSession().flush(); //清理缓存，执行批量插入
        this.getSession().clear(); //清空缓存中的 对象
    }
}
