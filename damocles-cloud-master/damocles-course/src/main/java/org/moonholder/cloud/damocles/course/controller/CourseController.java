package org.moonholder.cloud.damocles.course.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.moonholder.cloud.damocles.common.core.entity.Course;
import org.moonholder.cloud.damocles.common.core.entity.User;
import org.moonholder.cloud.damocles.common.core.entity.vo.ResponseEntity;
import org.moonholder.cloud.damocles.course.feign.SecurityFeign;
import org.moonholder.cloud.damocles.course.service.CourseService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * @ClassName TestController
 * @Description TODO
 * @Author qiang
 * @Date 2021/8/27 15:46
 * @Version 1.0
 **/
@RestController
@RequestMapping("course")
public class CourseController {

    private static final Logger logger=Logger.getLogger(String.valueOf(CourseController.class));

    @Resource
    private CourseService courseService;

    @Resource
    private SecurityFeign securityFeign;

    @GetMapping("myCourse/query")
    public ResponseEntity queryMyCourse(HttpServletRequest request, Page<Course> page, Course course)
    {
        User user=obtainCourseByRequest(request);
        course.setCteacherid(user.getId());
        return ResponseEntity.success(courseService.selectCoursePageByCondition(page,course));
    }

    @PostMapping("myCourse/create")
    public ResponseEntity createCourse(HttpServletRequest request, @RequestBody Course course)
    {
        User user=obtainCourseByRequest(request);
        course.setCteacherid(user.getId());
        return courseService.save(course)?ResponseEntity.success():ResponseEntity.error();
    }

    @PostMapping("myCourse/update")
    public ResponseEntity updateCourse(@RequestBody Course course)
    {
        return courseService.updateById(course)?ResponseEntity.success():ResponseEntity.error();
    }

    @DeleteMapping("myCourse/delete")
    public ResponseEntity deleteCourse(@Param("cid")Integer cid)
    {
        return courseService.removeById(cid)?ResponseEntity.success():ResponseEntity.error();
    }

    @GetMapping("myLesson/query")
    public ResponseEntity queryMyLesson(HttpServletRequest request, Page<Course> page)
    {
        User user=obtainCourseByRequest(request);
        int sid=user.getId();
        return ResponseEntity.success(courseService.selectMyLessonPage(page,sid));
    }

    @GetMapping("allCourse/query")
    public ResponseEntity queryAllCourse(HttpServletRequest request,Page<Course> page,Course course)
    {
        return ResponseEntity.success(courseService.selectCoursePageByCondition(page,course));
    }

    @PostMapping("allCourse/add")
    public ResponseEntity addMyCourse(HttpServletRequest request,@RequestBody Course course)
    {
        User currentUser=obtainCourseByRequest(request);
        int sid=currentUser.getId();
        courseService.addMyCourse(sid,course.getCid());
        return ResponseEntity.success();
    }

    private User obtainCourseByRequest(HttpServletRequest request)
    {
        User currentUser=securityFeign.findUserByRequest(request);
        return currentUser;
    }


}
