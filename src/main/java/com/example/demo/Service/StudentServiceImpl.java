package com.example.demo.Service;

import com.alibaba.fastjson.JSON;
import com.example.demo.mapper.StudentMapper;
import com.example.demo.model.Student;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Scanner;


@SuppressWarnings("all")
@Service("studentService")
public class StudentServiceImpl implements StudentService {

    private final Logger logger= LoggerFactory.getLogger(getClass());
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private RedisTemplate redisTemplate;

//    @Cacheable(cacheNames = {"student_cache"},key = "#id")
//    @Override
//    public Student queryStudentById(String id) {
//        System.out.println("数据库来的");
//        return studentMapper.selectByPrimaryKey(id);
//    }
    @Override
    public Student queryStudentById(String id){
        String key="student_cache"+id;

        //1.根据key查询redis缓存
        String studentJson= (String) redisTemplate.opsForValue().get(key);
        //2.判断缓存是否存在
        if(StringUtils.isNotBlank(studentJson)){
            //3.存在即返回
            Student student= JSON.parseObject(studentJson,Student.class);
            logger.info(studentJson+"  #redis来的");
            return student;
        }
        //4.缓存不存在，根据id查询数据库
        Student student=studentMapper.selectByPrimaryKey(id);
        if(student==null){
            logger.info("该名学生不存在");
        }
        else {
            studentJson=JSON.toJSONString(student);
            redisTemplate.opsForValue().set(key,studentJson);
            logger.info(studentJson+"  #mysql来的");
        }
        return student;
    }

    @Override
    public int updateByIdSelective(String id) {
        Student student=studentMapper.selectByPrimaryKey(id);
        if(student==null)return 0;
        String key="student_cache"+id;        //1.根据key查询redis缓存
        String studentJson= (String) redisTemplate.opsForValue().get(key);
        //2.判断缓存是否存在
        if(StringUtils.isNotBlank(studentJson)){
            //3.存在即删除
            redisTemplate.delete(key);
        }
        Scanner scanner=new Scanner(System.in);
        System.out.println("请输入修改后的 balance 值：");
        float stu_balance=scanner.nextFloat();
        student.setStuBalance(stu_balance);
        return studentMapper.updateByPrimaryKeySelective(student);
    }

    @Override
    public int deleteById(String id) {
        Student student=studentMapper.selectByPrimaryKey(id);
        if(student==null) {
            logger.info("该名学生不存在");
            return 0;
        }
        String key="student_cache"+id;        //根据key查询redis缓存
        String studentJson= (String) redisTemplate.opsForValue().get(key);
        //判断缓存是否存在
        if(StringUtils.isNotBlank(studentJson)){
            //存在即删除
            redisTemplate.delete(key);
            logger.info("redis缓存已删除");
        }
        return studentMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insertById(Student student) {
        return studentMapper.insert(student);
    }
}
