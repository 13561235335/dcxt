package cn.wzgzs.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wzgzs.springboot.common.RestResult;
@Controller
@RequestMapping("/index")
public class IndexController {
	

	/*
	 * RestResult 返回结果集
	 * {
	 * "code":1,
	 * "msg":"操作成功！",
	 * "data":"网站工作室"  
	 * }
	 * */
	@RequestMapping("/index")
	@ResponseBody
	public RestResult index(){
		
		return new RestResult("网站工作室");
	}
	
}
