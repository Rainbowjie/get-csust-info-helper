package com.yinbro.wx.pojo;

import java.util.List;

public class MenuBean {
	private List<ButtonBean> buttons;

	public void setButton(List<ButtonBean> button) {
		this.buttons = button;
	}

	public List<ButtonBean> getButton() {
		return this.buttons;
	}

}