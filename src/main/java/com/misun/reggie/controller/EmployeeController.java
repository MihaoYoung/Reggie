package com.misun.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.misun.reggie.common.R;
import com.misun.reggie.entity.Employee;
import com.misun.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;


    /**
     *
     * @param request Session相关
     * @param employee 将参数封装成Employee，前提是key对应
     * @return
     */
    @PostMapping("login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){


        // md5 加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // 查询 username 是否存在
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        // 若不存在
        if(emp == null){
            return R.error("登录失败");
        }


        // 密码比对，不一致
        if(!emp.getPassword().equals(password)){
            return R.error("登录失败");
        }

        // 查看员工状态
        if(emp.getStatus() == 0){
            return R.error("账号已禁用");
        }

        // 登录成功, 将员工ID存入Session并返回登录成功结果
        request.getSession().setAttribute("employee", emp.getId());

        return R.success(emp);
    }

    /**
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){

        // 清除Session中的员工ID
        request.getSession().removeAttribute("employee");

        return R.success("退出成功");
    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee){

        log.info("新增员工，员工信息:{}", employee.toString());

        // 设置初始密码123456，md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        // 公共字段
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());

        // 获取当前登录用户ID
        Long empId = (Long) request.getSession().getAttribute("employee");

        // 公共字段
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);

        employeeService.save(employee);

        return R.success("新增员工成功");
    }

    /**
     * 员工信息的分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){

        log.info("page={}, pageSize={}, name={}", page, pageSize, name);

        // 构造分页构造器
        Page pageInfo = new Page(page, pageSize);

        // 条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        // 过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        // 排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        // 执行查询
        employeeService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 根据id修改员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee){

        // 公共字段
//        employee.setUpdateTime(LocalDateTime.now());
//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateUser(empId);

        long id = Thread.currentThread().getId();
        log.info("线程id为：{}", id);

        employeeService.updateById(employee);

        return R.success("员工信息修改成功");
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){

        log.info("根据id查询员工信息");
        Employee employee = employeeService.getById(id);

        if(employee != null){
            return R.success(employee);
        }

        return R.error("没有查询到对应员工信息");
    }

}
