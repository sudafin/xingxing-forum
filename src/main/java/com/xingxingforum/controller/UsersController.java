package com.xingxingforum.controller;
import com.xingxingforum.entity.dto.admin.AdminDTO;
import com.xingxingforum.entity.dto.admin.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.xingxingforum.entity.R;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author huangdada
 * @since 2025-01-22
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class UsersController {

    @GetMapping()
    public R<String> test(){
        return R.ok("测试");
    }
    @PostMapping("")
    public R<String> test2(@RequestParam String name){
        return R.ok(name);
    }
    @PutMapping("")
    public R<String> test3(@RequestBody UserDTO userDTO){
        return R.ok(userDTO.getUserName());
    }
    @DeleteMapping("/{id}")
    public R<String> test4(@PathVariable int id){
        return R.ok(id+"");
    }
}
