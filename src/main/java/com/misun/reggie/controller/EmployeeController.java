package com.misun.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.misun.reggie.common.R;
import com.misun.reggie.entity.Employee;
import com.misun.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
}
