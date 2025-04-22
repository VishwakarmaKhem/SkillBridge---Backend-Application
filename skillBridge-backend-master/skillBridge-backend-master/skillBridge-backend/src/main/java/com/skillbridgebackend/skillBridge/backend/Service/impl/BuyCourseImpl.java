package com.skillbridgebackend.skillBridge.backend.Service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillbridgebackend.skillBridge.backend.Dto.BuyCourseDto;
import com.skillbridgebackend.skillBridge.backend.Dto.TopFiveCoursesDto;
import com.skillbridgebackend.skillBridge.backend.Entity.BuyCourse;
import com.skillbridgebackend.skillBridge.backend.Entity.Courses;
import com.skillbridgebackend.skillBridge.backend.Entity.User;
import com.skillbridgebackend.skillBridge.backend.Repository.BuyCourseRepository;
import com.skillbridgebackend.skillBridge.backend.Repository.CoursesRepository;
import com.skillbridgebackend.skillBridge.backend.Repository.UserRepository;
import com.skillbridgebackend.skillBridge.backend.Service.BuyCourseService;
import io.netty.util.internal.StringUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class BuyCourseImpl implements BuyCourseService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CoursesRepository coursesRepository;
    @Autowired
    private BuyCourseRepository buyCourseRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public BuyCourseImpl(UserRepository userRepository,
                         CoursesRepository coursesRepository,
                         BuyCourseRepository buyCourseRepository, ModelMapper mapper
            ,RedisTemplate<String, String> redisTemplate) {
        this.userRepository = userRepository;
        this.coursesRepository = coursesRepository;
        this.buyCourseRepository = buyCourseRepository;
        this.mapper = mapper;
        this.redisTemplate = redisTemplate;
    }


    //buy course method
    @Override
    public BuyCourse buyCourse(Long userId, Long courseId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Courses> courses = coursesRepository.findById(courseId);

        if (user.isEmpty()){
            throw new RuntimeException("user with this id does not found");
        }

        if(courses.isEmpty()){
            throw new RuntimeException("course with this id does not found");
        }

        Optional<BuyCourse> alreadyBuy = buyCourseRepository.findByUserIdAndCourseId(userId, courseId);

        BuyCourse buy;
        if (alreadyBuy.isPresent()){
            throw new RuntimeException("This course is already purchased by you, you can go and watch it");
        }else {
            buy = new BuyCourse();
            buy.setCourses(courses.get());
            buy.setUser(user.get());
            buy.setCourseName(courses.get().getCourseName());
            buy.setCourseBuyTimeStamp(new Date(System.currentTimeMillis()));
            buy.setCourseExpiry(LocalDateTime.now().plusMonths(3));
        }
        return buyCourseRepository.save(buy);
    }



    @Override
    public List<BuyCourseDto> getAllPurchasedCourse() {
        List<BuyCourse> buyCourseList = new ArrayList<>(buyCourseRepository.findAll());

        List<BuyCourseDto> buyCourseDtoList = buyCourseList.stream().map(buyCourse -> mapToDto(buyCourse)).toList();

        return buyCourseDtoList;
    }

    @Override
    public List<TopFiveCoursesDto> getTopFivePurchasedCourses() throws JsonProcessingException {
        String key = "topFiveCoursesForToday";
        String fromRedis = redisTemplate.opsForValue().get(key);

        if (fromRedis != null) {
            // If data is found in Redis, deserialize and returning it
            return objectMapper.readValue(fromRedis, new TypeReference<>() {
            });
        }

        List<Object[]> dailyTopFive = buyCourseRepository.topFiveCourses();
        List<TopFiveCoursesDto> dailyTopFiveCourses = new ArrayList<>();

        for (Object[] topCourse : dailyTopFive) {
            String courseName = (String) topCourse[0];
            Long dailyBuyCount = (Long) topCourse[1];

            TopFiveCoursesDto dto = new TopFiveCoursesDto(courseName, dailyBuyCount);
            dailyTopFiveCourses.add(dto);
        }

        String value = objectMapper.writeValueAsString(dailyTopFiveCourses);
        redisTemplate.opsForValue().set(key, value, 1, TimeUnit.HOURS);

        return dailyTopFiveCourses;
    }

    private BuyCourse mapToEntity(BuyCourseDto buyCourseDto){
        BuyCourse buyCourse = mapper.map(buyCourseDto, BuyCourse.class);
        return buyCourse;
    }

    //creating private method for converting entity to dto to use it again and again
    private BuyCourseDto mapToDto(BuyCourse buyCourse){
        //mapping using modelmapper maven
        BuyCourseDto buyCourseDto = mapper.map(buyCourse, BuyCourseDto.class);
        return buyCourseDto;
    }

    private TopFiveCoursesDto mapToTopFiveCourseDto(BuyCourse buyCourse){
        //mapping using modelmapper maven
        TopFiveCoursesDto topFiveCourseDto = mapper.map(buyCourse, TopFiveCoursesDto.class);
        return topFiveCourseDto;
    }
}
