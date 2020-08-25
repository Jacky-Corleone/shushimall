package com.camelot.ecm.sellercenter.malltheme;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ForkJoinPoolFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.camelot.basecenter.domain.AddressBase;
import com.camelot.basecenter.dto.AddressBaseDTO;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.common.Json;
import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.enmu.BasicEnum;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.sellercenter.malladvertise.dto.MallAdInDTO;
import com.camelot.sellercenter.malladvertise.dto.MallAdQueryDTO;
import com.camelot.sellercenter.malltheme.dto.MallThemeDTO;
import com.camelot.sellercenter.malltheme.service.MallThemeService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 主题页维护控制类
* @ClassName: MallThemeController 
* @Description: 主题页维护控制类
* @author 尹归晋
* @date 2015年10月29日 下午6:09:28 
*
 */
@Controller
@RequestMapping(value = "${adminPath}/sellercenter/mallTheme")
public class MallThemeController {
	
	//类目
	@Autowired
	private ItemCategoryService itemCategoryService;

	//地区
	@Autowired 
	private AddressBaseService addressBaseService;
	
	//主题
	@Autowired
	private MallThemeService mallThemeService;
	
	
	@RequestMapping(value = {"list", ""})
	public String list(MallThemeDTO mallThemeDTO,HttpServletRequest request,HttpServletResponse response,String msg,Model model){
		model.addAttribute("msg", msg);
		//查询一级类目
//		DataGrid<ItemCategoryDTO> categoryList = itemCategoryService.queryItemByCategoryLev(1);
//		//查询二级类目
//		DataGrid<ItemCategoryDTO> categoryLev2List = itemCategoryService.queryItemByCategoryLev(2);
		//查询地区省直辖市
		List<AddressBase> addresList = addressBaseService.queryAddressBase("0");
		//分页查询主题信息
		
//		User user = UserUtils.getUser();
//		if (user!=null) {
//			String id = user.getId();
//			//超级管理员无数据权限 查看所有 
////			if ("1"!=user.getUserType() && !"1".equals(user.getUserType())) {
//				mallThemeDTO.setUserId(id);
////			}
//		}
		//设置类目查询筛选
		String cid1 = mallThemeDTO.getPlatformId1();
		String cid2 = mallThemeDTO.getPlatformId2();
		if (cid1!=null && !"".equals(cid1)) {
			mallThemeDTO.setPrimaryCid(Long.parseLong(cid1));
		}
		if (cid2!=null && !"".equals(cid2)) {
			mallThemeDTO.setcId(Long.parseLong(cid2));
		}

		Page<MallThemeDTO> page = new Page<MallThemeDTO>(request, response);
		Pager pager = new Pager();
		pager.setPage(page.getPageNo());
		String pageSize = request.getParameter("pageSize");
		if(StringUtils.isNotBlank(pageSize) && StringUtils.isNumeric(pageSize)){
			pager.setRows(Integer.valueOf(pageSize));
		} else{
			pager.setRows(page.getMaxResults());
		}
		DataGrid<MallThemeDTO> mallThemeList = mallThemeService.queryMallThemeList(mallThemeDTO,null, pager);
		List<MallThemeDTO> list  = mallThemeList.getRows();
		List<String> codeArray = new ArrayList<String>();
		for(int i = 0;i<list.size();i++){
			MallThemeDTO mtd = list.get(i);
			if(mtd.getCityCode()!=null){
				codeArray.add(mtd.getCityCode().toString());
			}
			if(mtd.getVillageCode()!=null){
				codeArray.add(mtd.getVillageCode().toString());
			}
		}
		String[] codes = new String[codeArray.size()];
		for(int i =0 ;i<codes.length;i++){
			codes[i]=codeArray.get(i);
		}
		ExecuteResult<List<AddressBaseDTO>> addressArrayEr = null;
		if(codes.length>0){
			addressArrayEr = addressBaseService.queryNameByCode(codes);
		}
		page.setList(mallThemeList.getRows());
		page.setCount(mallThemeList.getTotal());
		page.setOrderBy(pager.getOrder());
		
		model.addAttribute("page",page);
		model.addAttribute("mallThemeDTO", mallThemeDTO);
//		model.addAttribute("categoryList", categoryList.getRows());
//		model.addAttribute("categoryLev2List", categoryLev2List.getRows());
		model.addAttribute("addresList", addresList);
		model.addAttribute("addressArray", addressArrayEr!=null?addressArrayEr.getResult():null);
		
		return "sellercenter/malltheme/list";
	}
	
	/**
	 * 编辑 或 添加 页
	* @Title: toForm 
	* @Description: 编辑 或 添加 页
	* @param request
	* @param mallThemeDTO
	* @param model
	* @return    设定文件 
	* @return String    返回类型 
	* @throws 
	* @author 尹归晋
	 */
	@RequestMapping("/toform")
	public String toForm(HttpServletRequest request,MallThemeDTO mallThemeDTO,Model model){
		MallThemeDTO dto = null;
		if (mallThemeDTO!=null && mallThemeDTO.getId()!=null) {
			dto = this.mallThemeService.getMallThemeById(mallThemeDTO.getId());
			if (dto.getClev()==2) {
				dto.setPlatformId2(String.valueOf((dto.getcId())));
				ExecuteResult<List<ItemCatCascadeDTO>> queryParentCategoryList = itemCategoryService.queryParentCategoryList(2,dto.getcId());
				List<ItemCatCascadeDTO> list = queryParentCategoryList.getResult();
				if (list!=null && list.size()>0) {
					ItemCatCascadeDTO itemCatCascadeDTO = list.get(0);
					dto.setPlatformId1(String.valueOf(itemCatCascadeDTO.getCid()));
				}
				dto.setcId(null);
			}
			model.addAttribute("mallThemeDTO", dto);
		}
		//查询一级类目
//		DataGrid<ItemCategoryDTO> categoryList = itemCategoryService.queryItemByCategoryLev(1);
//		model.addAttribute("categoryList", categoryList.getRows());
		//查询二级类目
//		if(dto != null && !StringUtils.isEmpty(dto.getPlatformId1())){
//			List<ItemCategoryDTO> cList = itemCategoryService.queryItemCategoryList(Long.parseLong(dto.getPlatformId1())).getRows();
//			model.addAttribute("categoryLev2List", cList);
//		}
		//查询地区省直辖市
		List<AddressBase> addresList = addressBaseService.queryAddressBase("0");
		model.addAttribute("addresList", addresList);
		return "sellercenter/malltheme/edit";
	}
	
	/**
	 * 添加或修改
	* @Title: addOrUpdate 
	* @Description: 添加或修改
	* @param mallThemeDTO
	* @param model
	* @return    设定文件 
	* @return String    返回类型 
	* @throws 
	* @author 尹归晋
	 */
	@RequestMapping("/addOrUpdate")
	public String addOrUpdate(MallThemeDTO mallThemeDTO,Model model){
		ExecuteResult<String> executeResult = new ExecuteResult<String>();
		String msg;
		if (mallThemeDTO.getcId()==null) {
			if (mallThemeDTO.getPlatformId2()!=null && !"".equals(mallThemeDTO.getPlatformId2())) {
				mallThemeDTO.setcId(Long.parseLong(mallThemeDTO.getPlatformId2()));
			}
		}
		//添加或修改为地区子站时设置cid cev 为空
		if (mallThemeDTO.getType()==3) {
			mallThemeDTO.setcId(null);
			mallThemeDTO.setClev(null);
		}
		if (mallThemeDTO.getType()==2) {
			mallThemeDTO.setAddressId(null);
		}
		if (mallThemeDTO.getType() == 1) {
			mallThemeDTO.setcId(null);
			mallThemeDTO.setClev(null);
			mallThemeDTO.setAddressId(null);
		}
		User user = UserUtils.getUser();
		mallThemeDTO.setUserId(user.getId());
		if(!org.apache.commons.lang3.StringUtils.isEmpty(mallThemeDTO.getPlatformId1())){
			mallThemeDTO.setPrimaryCid(Long.parseLong(mallThemeDTO.getPlatformId1()));
		}
		if(!org.apache.commons.lang3.StringUtils.isEmpty(mallThemeDTO.getPlatformId2())){
			mallThemeDTO.setcId(Long.parseLong(mallThemeDTO.getPlatformId2()));
		}
		if (mallThemeDTO.getId()!=null) {
			executeResult = this.mallThemeService.motifyMallTheme(mallThemeDTO);
			msg="editSuccess";
		}else {
			executeResult = this.mallThemeService.addMallTheme(mallThemeDTO);
			msg="addSuccess";
		}
		return "redirect:" + SysProperties.getAdminPath() + "/sellercenter/mallTheme/list?msg="+msg;
	}
	
	/**
	 * 删除
	* @Title: delete 
	* @Description: 删除
	* @param id
	* @return    设定文件 
	* @return String    返回类型 
	* @throws 
	* @author 尹归晋
	 */
	@RequestMapping("/delete")
	public String delete(Long id){
		ExecuteResult<String> delete = this.mallThemeService.delete(id);
		return "redirect:" + SysProperties.getAdminPath() + "/sellercenter/mallTheme/list?msg=delSuccess";
	}
	
	/**
	 * 
	 * <p>Discription:[下架]</p>
	 * Created on 2015年11月17日
	 * @param id
	 * @param status
	 * @return
	 * @author: 尹归晋
	 */
	@ResponseBody
	@RequestMapping("/upAndDown")
	public Json setStatusOne(Long id,String status){
		Json<Object> json=new Json<Object>();
        try{
        	this.mallThemeService.motifyMallThemeStatus(id, status);
            json.setMsg("操作完成");
            json.setSuccess(true);
        }catch(Exception e){
            json.setMsg("操作出现意外错误，请稍后再试");
            json.setSuccess(false);
        }
		return json;
	}
	
	
	/**
	 * 
	 * <p>Discription:[上架]</p>
	 * Created on 2015年11月17日
	 * @param id
	 * @param status
	 * @return
	 * @author: 尹归晋
	 */
	@ResponseBody
	@RequestMapping("/upStatus")
	public Json upStatus(Long id,String status,String primaryCid,String cid){
		Json<Object> json=new Json<Object>();
		//先判断是否有其他频道上架
		MallThemeDTO mallThemeDTO = new MallThemeDTO();
//		User user = UserUtils.getUser();
//		if (user!=null) {
//			String userid = user.getId();
//			mallThemeDTO.setUserId(userid);
//		}
		//设置类目查询筛选
		if (primaryCid!=null && !"".equals(primaryCid)) {
			mallThemeDTO.setPrimaryCid(Long.parseLong(primaryCid));
		}
		if (cid!=null && !"".equals(cid)) {
			mallThemeDTO.setcId(Long.parseLong(cid));
		}
		mallThemeDTO.setStatus(1);
		
        try{
        	this.mallThemeService.motifyMallThemeStatus(id, status);
            json.setMsg("操作完成");
            json.setSuccess(true);
        }catch(Exception e){
            json.setMsg("操作出现意外错误，请稍后再试");
            json.setSuccess(false);
        }
		return json;
	}
	
	/**
	 * 
	* @Title: queryCategory 
	* @Description: 查询类目列表信息 
	* @return    设定文件 
	* @return List<ItemCategoryDTO>    返回类型 
	* @throws 
	* @author 尹归晋
	 */
	@RequestMapping("/queryCategory")
	@ResponseBody
	public List<ItemCategoryDTO> queryCategory(Integer lev){
		if (lev!=null) {
			//类目级别查询类目
			DataGrid<ItemCategoryDTO> categoryList = itemCategoryService.queryItemByCategoryLev(lev);
			return categoryList.getRows();
		}else {
			return null;
		}
	}
	
	/**
	 * 
	 * <p>Discription:[查询地区]</p>
	 * Created on 2015年11月18日
	 * 			查省份，id = 0
	 * 			查省下面的市， id= 省份id
	 * 			查市下面的区/县， id= 市id
	 * @return
	 * @author: 尹归晋
	 */
	@ResponseBody
	@RequestMapping("/queryAddres")
	public List<AddressBase> queryAddres(String id){
		List<AddressBase> addresList = addressBaseService.queryAddressBase(id);
		return addresList;
	}
	/**
	 * 
	 * <p>Discription:[二级联动 根据类目id或地区code查询主题]</p>
	 * Created on 2015年11月17日
	 * @param cid
	 * @return
	 * @author: 尹归晋
	 */
	@RequestMapping("/queryTheme")
	@ResponseBody
	public List<MallThemeDTO> queryTheme(Long cid,Long addressId,Integer themeType){
		MallThemeDTO mallThemeDTO = new MallThemeDTO();
		User user = UserUtils.getUser();
		mallThemeDTO.setType(themeType);
		mallThemeDTO.setUserId(user.getId());
		if (null!=cid && 0!=cid) {
			mallThemeDTO.setcId(cid);
		}
		else if(null!=addressId && 0!=addressId){
			mallThemeDTO.setAddressId(addressId);
		}else{
			return null;
		}
		Pager<MallThemeDTO> page = new Pager<MallThemeDTO>();
		page.setPage(1);
		page.setRows(999);
		DataGrid<MallThemeDTO> mallThemeList = mallThemeService.queryMallThemeList(mallThemeDTO, BasicEnum.INT_ENUM_STATIC_ADDED.getStringValue(),page);
		return mallThemeList.getRows();
	}
	
	@ResponseBody
	@RequestMapping("/validCity")
	public ExecuteResult<String> validCity(Integer themeId,Long provinceCode,Long cityCode){
		ExecuteResult<String> result = new ExecuteResult<String>();
		Pager page = new Pager();
		page.setPage(1);
		page.setRows(999);
		MallThemeDTO mallThemeDTO = new MallThemeDTO();
		if (provinceCode==null || cityCode==null) {
			result.addErrorMessage("参数不正确");
			return result;
		}
		mallThemeDTO.setProvinceCode(provinceCode);
		mallThemeDTO.setCityCode(cityCode);
		DataGrid<MallThemeDTO> dataGrid = mallThemeService.queryMallThemeList(mallThemeDTO,null, page);
		
		if (dataGrid!=null && dataGrid.getRows()!=null && dataGrid.getRows().size()>0) {
			if(dataGrid.getRows().size()==1){
				if(themeId!=null){
					int to = dataGrid.getRows().get(0).getId().compareTo(themeId);
					if (to==0) {
						result.setResult("可以添加该信息！");
						return result;
					}
				}
				result.addErrorMessage("该地区已设置子站，无需再次添加，谢谢！");
				return result;
			}
			result.addErrorMessage("该地区已设置子站，无需再次添加，谢谢！");
			return result;
		}
		result.setResult("可以添加该信息！");
		return result;
	}
	
	@RequestMapping(value = {"adlist", ""})
	public String adlist(MallThemeDTO mallThemeDTO,MallAdQueryDTO statusDTO,HttpServletRequest request,HttpServletResponse response,String msg,Model model){
		model.addAttribute("msg", msg);
		MallAdInDTO dto = new MallAdInDTO();
		//查询一级类目
//		DataGrid<ItemCategoryDTO> categoryList = itemCategoryService.queryItemByCategoryLev(1);
//		//查询二级类目
//		DataGrid<ItemCategoryDTO> categoryLev2List = itemCategoryService.queryItemByCategoryLev(2);
		//查询地区省直辖市
		List<AddressBase> addresList = addressBaseService.queryAddressBase("0");
		//分页查询主题信息
		
//		User user = UserUtils.getUser();
//		if (user!=null) {
//			String id = user.getId();
//			//超级管理员无数据权限 查看所有 
////			if ("1"!=user.getUserType() && !"1".equals(user.getUserType())) {
//				mallThemeDTO.setUserId(id);
////			}
//		}
		//设置类目查询筛选
		String cid1 = mallThemeDTO.getPlatformId1();
		String cid2 = mallThemeDTO.getPlatformId2();
		if (cid1!=null && !"".equals(cid1)) {
			mallThemeDTO.setPrimaryCid(Long.parseLong(cid1));
		}
		if (cid2!=null && !"".equals(cid2)) {
			mallThemeDTO.setcId(Long.parseLong(cid2));
		}
		mallThemeDTO.setStatus(BasicEnum.INT_ENUM_STATIC_ADDED.getIntVlue());
		Page<MallThemeDTO> page = new Page<MallThemeDTO>(request, response);
		Pager pager = new Pager();
		pager.setPage(page.getPageNo());
		String pageSize = request.getParameter("pageSize");
		if(StringUtils.isNotBlank(pageSize) && StringUtils.isNumeric(pageSize)){
			pager.setRows(Integer.valueOf(pageSize));
		} else{
			pager.setRows(page.getMaxResults());
		}
		DataGrid<MallThemeDTO> mallThemeList = mallThemeService.queryMallThemeList(mallThemeDTO,null, pager);
		List<MallThemeDTO> list  = mallThemeList.getRows();
		List<String> codeArray = new ArrayList<String>();
		for(int i = 0;i<list.size();i++){
			MallThemeDTO mtd = list.get(i);
			if(mtd.getCityCode()!=null){
				codeArray.add(mtd.getCityCode().toString());
			}
			if(mtd.getVillageCode()!=null){
				codeArray.add(mtd.getVillageCode().toString());
			}
		}
		String[] codes = new String[codeArray.size()];
		for(int i =0 ;i<codes.length;i++){
			codes[i]=codeArray.get(i);
		}
		ExecuteResult<List<AddressBaseDTO>> addressArrayEr = null;
		if(codes.length>0){
			addressArrayEr = addressBaseService.queryNameByCode(codes);
		}
		page.setList(mallThemeList.getRows());
		page.setCount(mallThemeList.getTotal());
		page.setOrderBy(pager.getOrder());
		
		model.addAttribute("page",page);
		model.addAttribute("mallThemeDTO", mallThemeDTO);
		model.addAttribute("addresList", addresList);
		model.addAttribute("addressArray", addressArrayEr!=null?addressArrayEr.getResult():null);
		model.addAttribute("dto",dto);
	    model.addAttribute("statusDTO", statusDTO);
		return "/sellercenter/mallad/list";
	}
}
