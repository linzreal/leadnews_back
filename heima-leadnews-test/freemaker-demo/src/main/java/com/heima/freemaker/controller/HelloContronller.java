package com.heima.freemaker.controller;

import com.heima.freemaker.entity.Student;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
public class HelloContronller {


    @GetMapping("/basic")
    public String hello(Model model){

        model.addAttribute("name","freemaker");


        Student stu = new Student();
        stu.setName("xiaoming");
        stu.setAge(18);

        model.addAttribute("stu",stu);

        return "01-basic";
    }


    @GetMapping("/list")
    public String list(Model model){
        Student stu1 = new Student();
        stu1.setName("lzh");
        stu1.setAge(20);
        stu1.setMoney(10010.64f);
        stu1.setBirthday(new Date());

        Student stu2 = new Student();
        stu2.setName("zyq");
        stu2.setAge(25);
        stu2.setMoney(10010.64f);
        stu2.setBirthday(new Date());


        List<Student> list = new ArrayList<>();
        list.add(stu1);
        list.add(stu2);

        model.addAttribute("stus",list);


        Map<String, Student> map = new HashMap<>();
        map.put("stu1",stu1);
        map.put("stu2",stu2);

        model.addAttribute("stuMap",map);
        return "02-basic";
    }
}
