package com.example.demo.Service;

import com.example.demo.model.Student;

public interface StudentService {
    Student queryStudentById(String id);

    int updateByIdSelective(String id);
    int deleteById(String id);

    int insertById(Student student);
}
