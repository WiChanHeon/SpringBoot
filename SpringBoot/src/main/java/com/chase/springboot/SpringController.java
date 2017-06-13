package com.chase.springboot;

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
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView index(
			@ModelAttribute("formModel") MyData mydata,
			ModelAndView mav){
		mav.setViewName("index");
		mav.addObject("msg", "this is sample content.");
		Iterable<MyData> list = repository.findAll();
		mav.addObject("datalist", list);
		return mav;
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
}
