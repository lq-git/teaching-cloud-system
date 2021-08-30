package org.moonholder.cloud.damocles.course.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import org.moonholder.cloud.damocles.common.core.entity.Course;

/**
 * @ClassName CourseService
 * @Description TODO
 * @Author qiang
 * @Date 2021/8/27 20:35
 * @Version 1.0
 **/
public interface CourseService extends IService<Course> {
    IPage<Course> getMyCoursePageByTeacherId(Page<Course> page, Integer teacherId);

    IPage<Course> selectCoursePageByCondition(Page<Course> page, @Param("course")Course course);

    IPage<Course> selectMyLessonPage(Page<Course> page, @Param("sid") Integer sid);

    Integer addMyCourse(@Param("sid") Integer sid, @Param("cid") Integer cid);

}
