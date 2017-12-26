package org.yx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.yx.common.base.BaseController;
import org.yx.common.entity.Page;
import org.yx.common.entity.PageData;

/**
 * 
 * 2017-1-18
 * @author smz
 *
 */
@Controller
@RequestMapping(value="/login")
public class LoginController extends BaseController{

	@RequestMapping
	public ModelAndView list(Page page) throws Exception {
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		pd = getPageData();

		mv.setViewName("index");
		return mv;
	}
}
