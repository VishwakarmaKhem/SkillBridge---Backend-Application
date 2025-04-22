package com.skillbridgebackend.skillBridge.backend.Service.impl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillbridgebackend.skillBridge.backend.Dto.CourseResponse;
import com.skillbridgebackend.skillBridge.backend.Dto.CoursesDto;
import com.skillbridgebackend.skillBridge.backend.Dto.FeedbackDto;
import com.skillbridgebackend.skillBridge.backend.Entity.Courses;
import com.skillbridgebackend.skillBridge.backend.Exception.ResourceNotFoundException;
import com.skillbridgebackend.skillBridge.backend.Repository.CoursesRepository;
import com.skillbridgebackend.skillBridge.backend.Service.CoursesService;
import com.skillbridgebackend.skillBridge.backend.Utils.AppConstants;
import io.netty.util.internal.StringUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class CoursesServiceImpl implements CoursesService {

    @Autowired
    private CoursesRepository coursesRepository;

    private final RedisTemplate<String, Object> redisTemplate;


    @Autowired
    private ModelMapper mapper;


    @Autowired
    private ObjectMapper objectMapper;


    //constructor based dependency injection of repository
    public CoursesServiceImpl(CoursesRepository coursesRepository, ModelMapper mapper
                              ,RedisTemplate<String, Object> redisTemplate) {
        this.coursesRepository = coursesRepository;
        this.mapper = mapper;
        this.redisTemplate=redisTemplate;
    }


    private static final String COURSES_CACHE_PREFIX = "course:";

    @Override
    public CoursesDto createCourse(CoursesDto coursesDto) {
        //convert Dto to entity
        Courses courses = mapToEntity(coursesDto);

        //saving entity into database
        Courses newCourse =  coursesRepository.save(courses);

        //convert entity to Dto
        CoursesDto courseResponse = mapToDto(newCourse);

        //returning the response as dto to the client
        return courseResponse;
    }

    @Override
    public CoursesDto getCourseById(Long id) throws JsonProcessingException {

//        check redis first
        String key = COURSES_CACHE_PREFIX + id;
        String cachedCourses =(String) redisTemplate.opsForValue().get(key);
        Courses courses1=null;
       if(!StringUtil.isNullOrEmpty(cachedCourses)){
           courses1 = objectMapper.readValue(cachedCourses, Courses.class);
       }
        if (courses1!=null){
            return mapToDto(courses1);
        }

        //fetch from db
        Optional<Courses> courseDB = coursesRepository.findById(id);
        if(courseDB.equals(Optional.empty())){
            throw new ResourceNotFoundException("Courses", "id", id);
        }
        if (courseDB.isPresent()){
            Courses courses = courseDB.get();
            String s = objectMapper.writeValueAsString(courses);

            redisTemplate.opsForValue().set(key, s,120, TimeUnit.SECONDS);

            return mapToDto(courses);
        }

        return null;
    }

    @Override
    public CourseResponse getAllCourse(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortDir).descending();

        //creating pageable instance for pagination support(Note: if we are just giving pagination support and not sorting support then we have to use ".of" method with two parameters)
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        //retrieving data with pagination support from database and storing into a page
        Page<Courses> courses = coursesRepository.findAll(pageable);

        //retrieving the data from the page using getContent method of it and storing it into a list so that we can stream the data and map it to dto
        List<Courses> listOfCourses = courses.getContent();

        List<CoursesDto> content = listOfCourses.stream().map(course -> mapToDto(course)).collect(Collectors.toList());

        CourseResponse courseResponse = new CourseResponse();
        courseResponse.setContent(content);
        courseResponse.setPageNo(courses.getNumber());
        courseResponse.setPageSize(courses.getSize());
        courseResponse.setTotalElements(courses.getTotalElements());
        courseResponse.setTotalPages(courses.getTotalPages());
        courseResponse.setLast(courses.isLast());

        return courseResponse;
    }

    @Override
    public CoursesDto updateCourseById(Long id, CoursesDto coursesDto) {
        Courses courses = coursesRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Courses", "id", id));
        courses.setCourseName(coursesDto.getCourseName());
        courses.setCoursePrice(coursesDto.getCoursePrice());
        courses.setDescription(coursesDto.getDescription());
        courses.setCourseContent(coursesDto.getCourseContent());

        Courses updatedCourse = coursesRepository.save(courses);
        return mapToDto(updatedCourse);
    }

    @Override
    public void deleteCourseById(Long id) {
        Courses courses = coursesRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Courses", "id", id));
        coursesRepository.delete(courses);
    }


    //creating private method for converting dto to entity to use it again and again
    private Courses mapToEntity(CoursesDto coursesDto){
        //mapping using modelmapper maven
//        Courses courses = mapper.map(coursesDto, Courses.class);

        //mapping in normal code way
        Courses courses = new Courses();
        courses.setCourseName(coursesDto.getCourseName());
        courses.setCoursePrice(coursesDto.getCoursePrice());
        courses.setDescription(coursesDto.getDescription());
        courses.setCourseContent(coursesDto.getCourseContent());

        return courses;
    }

    //creating private method for converting entity to dto to use it again and again
    private CoursesDto mapToDto(Courses courses){
        //mapping using modelmapper maven

        CoursesDto coursesDto = mapper.map(courses, CoursesDto.class);

        Set<FeedbackDto> feedbackDtos = courses.getFeedbacks().stream()
                .map(feedback -> mapper.map(feedback, FeedbackDto.class))
                        .collect(Collectors.toSet());

        coursesDto.setFeedbacks(feedbackDtos);
        //mapping in normal code way
//        CoursesDto coursesDto = new CoursesDto();
//        coursesDto.setId(courses.getId());
//        coursesDto.setCourseName(courses.getCourseName());
//        coursesDto.setCoursePrice(courses.getCoursePrice());
//        coursesDto.setDescription(courses.getDescription());
//        coursesDto.setCourseContent(courses.getCourseContent());


        return coursesDto;
    }
}
