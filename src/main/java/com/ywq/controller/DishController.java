package com.ywq.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ywq.annotations.MethodCount;
import com.ywq.common.ResponseTemplate;
import com.ywq.dto.DishDto;
import com.ywq.entity.Category;
import com.ywq.entity.Dish;
import com.ywq.service.CategoryService;
import com.ywq.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;


    /**
     * 新增菜品
     *
     * @param dish
     * @return String
     */
    @PostMapping
    public ResponseTemplate<String> addDish(@RequestBody Dish dish) {
        try {
            log.info("Add new dish {}", dish.toString());
            boolean result = dishService.addNewDish(dish);
            return ResponseTemplate.success("Add successfully");
        } catch (Exception e) {
            log.error("Add failed {}", e.toString());
            return ResponseTemplate.error("failed");
        }
    }

    @PutMapping
    public ResponseTemplate<String> updateDish(@RequestBody Dish dish) {
        try {
            log.info("Update dish {}", dish.toString());
            dishService.updateDish(dish);
            return ResponseTemplate.success("Update successfullt");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseTemplate.error(e.toString());
        }
    }

    @GetMapping("/page")
    public ResponseTemplate<Page> getDishPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        log.info("Get Dish page: {}", pageNum, pageSize);
        Page<Dish> pageInfo = new Page<>(pageNum, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        LambdaQueryWrapper<Dish> dishWrapper = new LambdaQueryWrapper<>();
        dishWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo, dishWrapper);

        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> dishDtoList = records.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(dishDtoList);
        return ResponseTemplate.success(dishDtoPage);
    }

    @GetMapping("/{id}")
    public ResponseTemplate<DishDto> getDishById(@PathVariable Long id) {
        log.info("get dish by id:{}", id);
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return ResponseTemplate.success(dishDto);
    }

    @MethodCount
    @GetMapping("/list")
    public ResponseTemplate<List<Dish>> listDish() {
        log.info("get dish list");
        List<Dish> list = dishService.list();
        return ResponseTemplate.success(list);
    }

    @PostMapping("/submit")
    public ResponseTemplate<String> submitDish(@RequestBody List<DishDto> dishDtoList) {
        log.info("submit dish list: {} ",dishDtoList.toString());
        dishService.submitDishList(dishDtoList);
        return ResponseTemplate.success("submit successfully");
    }
}
