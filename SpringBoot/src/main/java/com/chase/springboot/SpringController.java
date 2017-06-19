package com.chase.springboot;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.chase.springboot.repositories.MyDataRepository;

@Controller
public class SpringController {
	
	@Autowired
	MyDataRepository repository;
	
	@PersistenceContext
	EntityManager entityManager;
	
	MyDataDaoImpl dao;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView index(ModelAndView mav){
		mav.setViewName("index");
		mav.addObject("msg", "MyDATA의 예제입니당.");
		Iterable<MyData> list = dao.getAll();
		mav.addObject("datalist", list);
		return mav;
	}
	
	@PostConstruct
	public void init(){
		dao = new MyDataDaoImpl(entityManager);
		MyData d1 = new MyData();
		d1.setName("chase1");
		d1.setAge(123);
		d1.setMail("c52871@naver.com");
		d1.setMemo("1234567891");
		repository.save(d1);
		
		MyData d2 = new MyData();
		d2.setName("chase2");
		d2.setAge(15);
		d2.setMail("c52872@naver.com");
		d2.setMemo("1234567892");
		repository.save(d2);
		
		MyData d3 = new MyData();
		d3.setName("chase3");
		d3.setAge(37);
		d3.setMail("c52873@naver.com");
		d3.setMemo("1234567893");
		repository.save(d3);
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	@Transactional(readOnly=false)
	public ModelAndView form(
			@ModelAttribute("formModel") 
			@Validated MyData mydata,
			BindingResult result,
			ModelAndView mov){
		ModelAndView res = null;
		if(!result.hasErrors()){
			repository.saveAndFlush(mydata);
			res = new ModelAndView("redirect:/");
		}else{
			mov.setViewName("index");
			mov.addObject("msg","sry, error is occured...");
			Iterable<MyData> list = repository.findAll();
			mov.addObject("datalist", list);
			res = mov;
		}
		return res; 
	}
	
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public ModelAndView edit(
			@ModelAttribute MyData mydata,
			@PathVariable int id,
			ModelAndView mav){
		mav.setViewName("edit");
		mav.addObject("title", "edit mydata.");
		MyData data = repository.findById((long)id);
		mav.addObject("formModel", data);
		return mav;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@Transactional(readOnly=false)
	public ModelAndView update(
			@ModelAttribute MyData mydata,
			ModelAndView mav){
		repository.saveAndFlush(mydata);
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public ModelAndView delete(
			@PathVariable int id,
			ModelAndView mav){
		mav.setViewName("delete");
		mav.addObject("title", "delete mydata.");
		MyData data = repository.findById((long)id);
		mav.addObject("formModel", data);
		return mav;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@Transactional(readOnly=false)
	public ModelAndView remove(
			@RequestParam long id,
			ModelAndView mav){
		repository.delete(id);
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping(value = "/find", method = RequestMethod.GET)
	public ModelAndView find(ModelAndView mav){
		mav.setViewName("find");
		mav.addObject("title", "Find Page");
		mav.addObject("msg", "MyData의 예제입니다.");
		mav.addObject("value", "");
		Iterable<MyData> list = dao.getAll();
		mav.addObject("datalist", list);
		return mav;
	}
	
	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public ModelAndView search(HttpServletRequest request,
			ModelAndView mav){
		mav.setViewName("find");
		
		String param = request.getParameter("fstr");
		
		if(param == ""){
			mav = new ModelAndView("redirect:/find");
		}else{
			mav.addObject("title", "Find Result");
			mav.addObject("msg", param + "의 검색 결과");
			mav.addObject("value", param);
			List<MyData> list = dao.find(param);
			mav.addObject("datalist", list);
		}
		
		return mav;
	}
}
