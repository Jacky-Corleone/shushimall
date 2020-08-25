package com.thinkgem.jeesite.modules.cms.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.modules.cms.entity.FileTpl;

/**
 * 
 * @author lj
 * 按静态化需求实现获取文件路径服务类
 *
 */
@Service
@Transactional(readOnly = true)
public class FileStaticTplService {

    public List<String> getNameListByPrefix(String path) {
        List<FileTpl> list = getListByPath(path, false);
        List<String> result = new ArrayList<String>(list.size());
        for (FileTpl tpl : list) {
            result.add(tpl.getName());
        }
        return result;
    }

    public List<FileTpl> getListByPath(String path, boolean directory) {
   		File f = new File(path);
   		if (f.exists()) {
   			File[] files = f.listFiles();
   			if (files != null) {
   				List<FileTpl> list = new ArrayList<FileTpl>();
   				for (File file : files) {
                    if(file.isFile() || directory)
   					    list.add(new FileTpl(file, ""));
   				}
   				return list;
   			} else {
   				return new ArrayList<FileTpl>(0);
   			}
   		} else {
   			return new ArrayList<FileTpl>(0);
   		}
   	}
}
