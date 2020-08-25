package com.camelot.goodscenter.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.camelot.goodscenter.domain.Item;
import com.camelot.goodscenter.domain.ItemSku;
import com.camelot.goodscenter.dto.ItemAdDTO;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemQueryInDTO;
import com.camelot.goodscenter.dto.ItemQueryOutDTO;
import com.camelot.goodscenter.dto.ItemShopCartDTO;
import com.camelot.goodscenter.dto.ItemShopCidDTO;
import com.camelot.goodscenter.dto.ItemStatusModifyDTO;
import com.camelot.goodscenter.dto.SellPrice;
import com.camelot.goodscenter.dto.SkuInfo;
import com.camelot.goodscenter.dto.SkuPictureDTO;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;

/**
 * 
 * <p>Description: [閹诲繗鍫拠銉ц濮掑倽顪呴崝绮�dao]</p>
 * Created on 2015楠烇拷閺堬拷2閺冿拷
 * @author  <a href="mailto: xxx@camelotchina.com">chenx</a>
 * @version 1.0 
 * Copyright (c) 2015 閸栨ぞ鍚弻顖濆箞閻楀湱顬愰幎锟芥箒闂勬劕鍙曢崣锟芥禍銈勭帛闁拷
 */
public interface ItemMybatisDAO extends BaseDAO<Item> {
	/**
	 * 
	 * <p>Discription:[閺傝纭堕崝鐔诲厴娑擃厽鏋冮幓蹇氬牚:閺屻儴顕楅崯鍡楁惂娣団剝浼呴惃鍕灙鐞涒暀</p>
	 * Created on 2015楠烇拷閺堬拷閺冿拷
	 * @param itemInDTO 
	 * @param page
	 * @return
	 * @author:[chenx]
	 */
	public List<ItemQueryOutDTO> queryItemList(@Param("entity") ItemQueryInDTO itemInDTO, @Param("page") Pager<ItemQueryOutDTO> page);

	/**
	 * 
	 * <p>Discription:[閺傝纭堕崝鐔诲厴娑擃厽鏋冮幓蹇氬牚:閺屻儴顕楅崯鍡楁惂娣団剝浼呴惃鍕灙鐞涒暀</p>
	 * Created on 2016 张庆楠
	 * @param itemInDTO 
	 * @param page
	 * @return
	 * @author:[chenx]
	 */
	public List<ItemQueryOutDTO> queryItemListByCreateTime(@Param("entity") ItemQueryInDTO itemInDTO, @Param("page") Pager<ItemQueryOutDTO> page);

	/**
	 * 
	 * <p>Discription:[批量更改商品状态]</p>
	 * Created on 2015-3-16
	 * @param ids
	 * @param changeReason 商品
	 * @param itemStatus
	 * @author:wangcs
	 */
	public void updateItemStatusBatch(@Param("ids") List<Long> ids, @Param("changeReason") String changeReason,
			@Param("itemStatus") int itemStatus);

	
	/**
	 * 
	 * <p>Discription:[閺傝纭堕崝鐔诲厴娑擃厽鏋冮幓蹇氬牚:閺屻儴顕楅崯鍡楁惂閻ㄥ嫭锟介弫浼村櫤]</p>
	 * Created on 2015楠烇拷閺堬拷閺冿拷
	 * @param itemInDTO
	 * @return
	 * @author:[chenx]
	 */
	public Long queryCount(@Param("entity") ItemQueryInDTO itemInDTO);
	/**
	 * 
	 * <p>Discription:[閺傝纭堕崝鐔诲厴娑擃厽鏋冮幓蹇氬牚:閺嶈宓乮d閺屻儴顕涢幆鍖�/p>
	 * Created on 2015楠烇拷閺堬拷閺冿拷
	 * @param itemId
	 * @return
	 * @author:[chenx]
	 */
	public ItemDTO getItemDTOById(@Param("id") Long itemId);

	/**
	 * 
	 * <p>Discription:[閺嶈宓侀崯鍡楁惂ID閺屻儴顕楅崯鍡楁惂娑撳娈慡KU娣団剝浼呴妴鍌氬瘶閹风悞KU娴犻攱鐗搁妴涔籯u閸ュ墽澧朷</p>
	 * Created on 2015楠烇拷閺堬拷閺冿拷
	 * @param itemId
	 * @return
	 * @author:[閸掓稑缂撻懓鍛厬閺傚洤鎮曠�姊�
	 */
	public List<SkuInfo> queryItemSkuInfo(@Param("id") Long itemId);

	/**
	 * <p>Discription:[娣囨繂鐡ㄩ崯鍡楁惂閻ㄥ嫬鐔�張顑夸繆閹垰鎷扮猾鑽ゆ窗鐏炵偞锟介柅澶夎厬閻ㄥ嫮娈戦柨顔硷拷缂佸垟</p>
	 * Created on 2015楠烇拷閺堬拷閺冿拷
	 * @param item
	 * @return
	 * @author:[閺夈劑妲糫
	 */
	public void addItem(Item item);
	
	
	/**
	 * 
	 * <p>Discription:[更新商品信息]</p>
	 * Created on 2015-3-6
	 * @param itemDTO
	 * @author:wangcs
	 */
	public void updateItem(ItemDTO itemDTO);

	/**
	 * 
	 * <p>Discription:[閹绘帒鍙嗛崯鍡楁惂闂冭埖顤婃禒绌�/p>
	 * Created on 2015楠烇拷閺堬拷閺冿拷
	 * @param sellPrice
	 * @author:[wangcs]
	 */
	public void insertItemPrice(@Param("list") List<SellPrice> sellPrice,@Param("itemId") Long itemId);


	/**
	 * <p>Discription:[鏍规嵁鍏抽敭瀛楁煡璇㈠晢鍝佺殑鏁伴噺]</p>
	 * Created on 2015骞�鏈�鏃�
	 * @param keyword 鎼滅储鍏抽敭瀛�
	 * @return
	 * @author:[鍛ㄤ箰]
	 */
	public long queryItemCounts(@Param("params") Map<String, Object> params);

	/**
	 * 
	 * <p>Discription:[根据SKU id 查询SKU信息 包括商品名称]</p>
	 * Created on 2015-3-4
	 * @param skuDTO
	 * @return
	 * @author:wangcs
	 */
	public ItemSku getItemSkuById(@Param("param") ItemShopCartDTO skuDTO);

	/**
	 * 
	 * <p>Discription:[批量修改商品广告语]</p>
	 * Created on 2015-3-10
	 * @param ads
	 * @author:wangcs
	 */
	public void modifyItemAdBatch(@Param("ads") List<ItemAdDTO> ads);

	/**
	 * 
	 * <p>Discription:[批量修改商品的店铺分类]</p>
	 * Created on 2015-3-10
	 * @param cids
	 * @author:wangcs
	 */
	public void modifyItemShopCidBatch(@Param("cids") List<ItemShopCidDTO> cids);

	/**
	 * 
	 * <p>Discription:[查询SKU图片]</p>
	 * Created on 2015-3-13
	 * @param skuId
	 * @return
	 * @author:wangcs
	 */
	public List<SkuPictureDTO> querySkuPics(@Param("skuId") Long skuId);

	/**
	 * 
	 * <p>Discription:[获取商品ID]</p>
	 * Created on 2015-3-14
	 * @return
	 * @author:wangcs
	 */
	public Long getItemId();

	/**
	 * 
	 * <p>Discription:[根据平台库商品ID查询商品衍生出的]</p>
	 * Created on 2015-3-16
	 * @param itemId
	 * @return
	 * @author:wangcs
	 */
	
	public List<ItemDTO> querySellerItems(@Param("itemId") Long itemId);


	/**
	 * 
	 * <p>Description: [根据商品的非销售属性查询商品信息]</p>
	 * Created on 2015年8月16日
	 * @param attr key-value形式
	 * @return
	 * @author:[宋文斌]
	 */
	public List<ItemDTO> queryItemDTOByAttr(@Param("attr") String attr);

	/**
	 * 
	 * <p>Discription:[查询商品列表  返回ItemDTO对象]</p>
	 * Created on 2015-3-19
	 * @param itemDTO
	 * @param pager
	 * @return
	 * @author:wangcs
	 */
//	public List<ItemDTO> queryItemDTOList(@Param("entity") SearchInDTO inDTO,@Param("page")Pager<ItemDTO> pager);

	/**
	 * 
	 * <p>Discription:[查询商品总条数]</p>
	 * Created on 2015-3-19
	 * @param itemDTO
	 * @return
	 * @author:wangcs
	 */
//	public Long queryItemDTOCount(@Param("entity") SearchInDTO inDTO);

	/**
	 * 
	 * <p>Discription:[根据平台商品ID 查询卖该商品的店铺]</p>
	 * Created on 2015-3-20
	 * @param itemId
	 * @param pager
	 * @return
	 * @author:wangcs
	 */
	public List<Long> queryShopIdByPlatItemId(@Param("itemId") Long itemId, @Param("page") Pager<Long> pager);

	/**
	 * 
	 * <p>Discription:[根据平台商品ID 查询卖该商品的店铺总数量]</p>
	 * Created on 2015-3-20
	 * @param itemId
	 * @return
	 * @author:wangcs
	 */
	public Long queryShopCountByPlatItemId(@Param("itemId") Long itemId);

	/**
	 * 
	 * <p>Discription:[商品库待入库已入库状态修改]</p>
	 * Created on 2015-3-24
	 * @param ids
	 * @param status
	 * @author:wangcs
	 */
	public void updateItemPlatStatus(@Param("ids") List<Long> ids, @Param("status") Integer status);

	/**
	 * 
	 * <p>Discription:[按照店铺ID修改商品状态]</p>
	 * Created on 2015-4-20
	 * @param statusDTO
	 * @author:wangcs
	 */
	public void updateShopItemStatus(ItemStatusModifyDTO statusDTO);
	
	/**
	 * 
	 * <p>Description: [根据商品名称和店铺ID查询商品,非模糊查询]</p>
	 * Created on 2015年8月17日
	 * @param itemName
	 * @return
	 * @author:[宋文斌]
	 */
	public List<ItemDTO> queryItemByItemNameAndShopId(@Param("itemName") String itemName,@Param("shopId") Long shopId);
	
    /**
     * 
     * <p>Discription:[更新上架时间，为空时更新调此方法]</p>
     * Created on 2015-10-30
     * @param ItemDTO
     * @author:[王鹏]
     */
	public void  updateTimingListing(ItemDTO itemDTO);
	
	/**
	 * <p>Discription:[根据itemId 串，查询对应的item 数据信息]</p>
	 * Created on 2015年11月3日
	 * @param iids
	 * @return
	 * @author:[王鹏]
	 */
	public List<ItemDTO> getItemDTOByItemIds(@Param("iids") Long[] iids);
	
	/**
	 * <p>更新商品的是否置顶状态</p>
	 * Created on 2016年2月22日
	 * @param items
	 * @param placedTop
	 * @return
	 * @author: 顾雨
	 */
	int updatePlacedTop(@Param("items")List<Long> items, @Param("placedTop")Integer placedTop);
	/**
	 * 
	 * <p>Discription:[更新商品信息,没有where条件判断，属性为空时也进行更新]</p>
	 * Created on 2016-3-10
	 * @param itemDTO
	 * @author:wangpeng
	 */
	public void update(ItemDTO itemDTO);

}