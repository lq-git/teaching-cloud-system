package org.moonholder.cloud.damocles.course.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.moonholder.cloud.damocles.common.core.entity.Course;
import org.moonholder.cloud.damocles.course.mapper.CourseMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName CourseServiceImpl
 * @Description TODO
 * @Author qiang
 * @Date 2021/8/27 20:40
 * @Version 1.0
 **/
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Resource
    private CourseMapper courseMapper;

    @Override
    public IPage<Course> getMyCoursePageByTeacherId(Page<Course> page, Integer teacherId) {
        QueryWrapper<Course> wrapper=new QueryWrapper<>();
        wrapper.eq("cteacherid",teacherId);
        return  courseMapper.selectPage(page,wrapper);
    }

    @Override
    public IPage<Course> selectCoursePageByCondition(Page<Course> page, @Param("course")Course course) {
        return baseMapper.selectCoursePageByCondition(page,course);
    }

    @Override
    public IPage<Course> selectMyLessonPage(Page<Course> page, @Param("sid")Integer sid) {
        return baseMapper.selectMyLessonPage(page,sid);
    }

    @Override
    public Integer addMyCourse(Integer sid,Integer cid) {
        QueryWrapper<Course> wrapper=new QueryWrapper<>();
        wrapper.eq("cid",cid);
        Course course=courseMapper.selectById(cid);
        course.setCselcount(course.getCselcount()+1);
        Map<String,Object> map=new HashMap<>();
        map.put("cid",cid);
        map.put("sid",sid);
        return baseMapper.addMyCourse(map);
    }
}
