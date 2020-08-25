package com.camelot.ecm.goodscenter.view;

import com.camelot.goodscenter.dto.ItemQueryOutDTO;
import com.camelot.settlecenter.dto.SettleItemExpenseDTO;
import com.camelot.storecenter.dto.ShopDTO;
/**
 * 
 * @author songhaisong
 *
 */
public class ItemQueryOutView {
	private ItemQueryOutDTO itemQueryOutDTO;
	private ShopDTO shopDTO;
	private String category;
    private SettleItemExpenseDTO settleItemExpenseDTO;
    private boolean isChecked;//商品是否被选中

    public SettleItemExpenseDTO getSettleItemExpenseDTO() {
        return settleItemExpenseDTO;
    }

    public void setSettleItemExpenseDTO(SettleItemExpenseDTO settleItemExpenseDTO) {
        this.settleItemExpenseDTO = settleItemExpenseDTO;
    }

    public ItemQueryOutDTO getItemQueryOutDTO() {
		return itemQueryOutDTO;
	}
	public void setItemQueryOutDTO(ItemQueryOutDTO itemQueryOutDTO) {
		this.itemQueryOutDTO = itemQueryOutDTO;
	}
	public ShopDTO getShopDTO() {
		return shopDTO;
	}
	public void setShopDTO(ShopDTO shopDTO) {
		this.shopDTO = shopDTO;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	
}
