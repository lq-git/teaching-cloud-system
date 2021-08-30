package org.moonholder.cloud.damocles.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.moonholder.cloud.damocles.common.core.entity.Course;
import org.omg.CORBA.INTERNAL;

import java.util.Map;

/**
 * @ClassName CourseMapper
 * @Description TODO
 * @Author qiang
 * @Date 2021/8/27 20:35
 * @Version 1.0
 **/
public interface CourseMapper extends BaseMapper<Course> {
    IPage<Course> selectCoursePageByCondition(Page<Course> page, @Param("course")Course course);

    IPage<Course> selectMyLessonPage(Page<Course> page, @Param("sid") Integer sid);

    Integer addMyCourse(Map<String,Object> map);
}
