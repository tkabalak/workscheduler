package com.chmura.exam.workingscheduler.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("admin")
public class AdminController {
    
    
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public void addManager() {
        
    }
    
    @RequestMapping(value = "/remove/{id}", method = RequestMethod.DELETE)
    public void remove(@PathVariable("id") long id, Model model) {
        
        
    }
    
    
}
