/*
 * Copyright 2015-2016 RonCoo(http://www.roncoo.com) Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.roncoo.adminlte.controller.admin;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.roncoo.adminlte.bean.entity.RcDataDictionaryList;
import com.roncoo.adminlte.bean.vo.Result;
import com.roncoo.adminlte.biz.DataDictionaryListBiz;
import com.roncoo.adminlte.util.base.BaseController;
import com.roncoo.adminlte.util.base.Page;
import com.roncoo.adminlte.util.base.ParamUtil;

/**
 * 数据字典明细Controller
 * 
 * @author LYQ
 */
@Controller
@RequestMapping(value = "/admin/dataDictionaryList", method = RequestMethod.POST)
public class DataDictionaryListController extends BaseController {

	@Autowired
	private DataDictionaryListBiz biz;

	/**
	 * 分页查询
	 * 
	 * @param modelMap
	 * @param pageCurrent
	 * @param pageSize
	 * @param id
	 * @param fieldCode
	 */
	@RequestMapping(value = LIST, method = RequestMethod.GET)
	public void list(ModelMap modelMap, @RequestParam(defaultValue = "1") int pageCurrent, @RequestParam(defaultValue = "3") int pageSize, HttpServletRequest request) {
		// modelMap.put("date", date);
		// modelMap.put("search", search);
		// modelMap.put("dId", dId);
		// modelMap.put("fieldCode", fieldCode);
		
		Map<String, Object> params = ParamUtil.getParamsMap(request, null);
		modelMap.put("param", params);

		Result<Page<RcDataDictionaryList>> result = biz.listForPage(pageCurrent, pageSize, (String) params.get("fieldCode"), (String) params.get("date"), (String) params.get("search"));
		if (result.isStatus()) {
			modelMap.put("page", result.getResultData());
		}
		
		String paramUrl = ParamUtil.getParamUrl(request, params, "pageCurrent");
		modelMap.put("paramUrl", paramUrl);
	}

	/**
	 * 保存
	 * 
	 * @param rcDataDictionaryList
	 * @param bindingResult
	 * @param dId
	 * @return
	 */
	@RequestMapping(value = SAVE)
	public String save(@ModelAttribute("dListVo") RcDataDictionaryList rcDataDictionaryList, BindingResult bindingResult, @RequestParam(name = "dId") Long dId) {
		if (!bindingResult.hasErrors()) {
			biz.save(rcDataDictionaryList);
		}
		return redirect("/admin/dataDictionaryList/list?dId={0}&fieldCode={1}", dId, rcDataDictionaryList.getFieldCode());
	}

	/**
	 * 删除
	 * 
	 * @param id
	 * @param dId
	 * @param fieldCode
	 * @return
	 */
	@RequestMapping(value = DELETE, method = RequestMethod.GET)
	public String delete(@RequestParam(value = "id") Long id, @RequestParam(value = "dId") Long dId, @RequestParam(value = "fieldCode") String fieldCode) {
		Result<String> result = biz.deleteById(id);
		if (result.isStatus()) {
			return redirect("/admin/dataDictionaryList/list?id={0}&fieldCode={1}", dId, fieldCode);
		}
		return fieldCode;
	}

	/**
	 * 查看
	 * 
	 * @param id
	 * @param modelMap
	 */
	@RequestMapping(value = VIEW, method = RequestMethod.GET)
	public void view(@RequestParam(value = "id") Long id, ModelMap modelMap) {
		Result<RcDataDictionaryList> result = biz.queryById(id);
		if (result.isStatus()) {
			modelMap.put("dictionaryList", result.getResultData());
		}
	}

	/**
	 * 修改
	 * 
	 * @param id
	 * @param dId
	 * @param modelMap
	 */
	@RequestMapping(value = EDIT, method = RequestMethod.GET)
	public void edit(@RequestParam(value = "id", defaultValue = "-1") Long id, @RequestParam(value = "dId") Long dId, ModelMap modelMap) {
		Result<RcDataDictionaryList> result = biz.queryById(id);
		modelMap.put("dId", dId);
		if (result.isStatus()) {
			modelMap.put("dictionaryList", result.getResultData());
		}
	}

	/**
	 * 更新
	 * 
	 * @param rcDataDictionaryList
	 * @param dId
	 * @return
	 */
	@RequestMapping(value = UPDATE)
	public String update(@ModelAttribute RcDataDictionaryList rcDataDictionaryList, @RequestParam(value = "dId") Long dId) {
		Result<RcDataDictionaryList> result = biz.update(rcDataDictionaryList);
		if (result.isStatus()) {
			return redirect("/admin/dataDictionaryList/list?dId={0}&fieldCode={1}", dId, rcDataDictionaryList.getFieldCode());
		}
		return null;
	}
}