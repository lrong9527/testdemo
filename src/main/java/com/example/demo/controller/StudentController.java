package com.example.demo.controller;

import com.example.demo.Service.StudentService;
import com.example.demo.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SuppressWarnings("all")
@Controller
public class StudentController {

    private final Logger logger= LoggerFactory.getLogger(getClass());
    @Autowired
    private StudentService studentService;

    @RequestMapping("/student")
    @ResponseBody
    public Object student(String id){
        Student student=studentService.queryStudentById(id);
        if(student==null)return "{查询失败！！}";
        return student;
    }

    @RequestMapping ("/update")
    @ResponseBody
    public String update(String id){
        int count=studentService.updateByIdSelective(id);
        if(count>0){
            logger.info("更新成功");
            return "更新成功";
        }
        return "更新失败";
    }

    @RequestMapping ("/delete")
    @ResponseBody
    public String delete(String id){
        int count=studentService.deleteById(id);
        if(count>0){
            logger.info("mysql已删除");
            return "删除成功";
        }
        return "删除失败或查无此人";
    }
    @RequestMapping ("/insert")
    @ResponseBody
    public String insert(String id){

        Student stu = studentService.queryStudentById(id);
        if(stu!=null){
            logger.info("数据库已存在");
            return "用户名已存在";
        }
        Student student=new Student();
        String name="谷歌";
        int score=350;
        float balance=365.04f;
        student.setStuId(id);
        student.setStuName(name);
        student.setStuScore(score);
        student.setStuBalance(balance);
        int count = studentService.insertById(student);
        if(count>0){
            logger.info("成功录入数据库");
            return "插入成功";
        }

        logger.info("录入失败");
        return "插入失败";
    }
}
