package io.github.xxyopen.novel.controller.front;

import io.github.xxyopen.novel.core.constant.ApiRouterConsts;
import io.github.xxyopen.novel.core.common.resp.RestResp;
import io.github.xxyopen.novel.dto.resp.HomeBookRespDto;
import io.github.xxyopen.novel.dto.resp.HomeFriendLinkRespDto;
import io.github.xxyopen.novel.service.HomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 前台门户-首页模块 API 控制器
 *
 * @author chenxi
 * @date 2022/5/12
 */

/**
 * @Tag()是一个用于OpenAPI/Swagger文档生成的注解。它表示该控制器的名称为"HomeController"，描述为"前台门户-首页模块"。
 *
 * @RestController表示该类是一个控制器，用于处理客户端的HTTP请求并返回相应的响应结果。
 * 相对于传统的@Controller注解，@RestController注解还会自动将方法返回的对象转换为JSON等格式的数据，并返回给客户端。
 *
 * @RequestMapping()，用于指定该控制器处理的请求路径的前缀。
 * ${ApiRouterConsts.API_FRONT_HOME_URL_PREFIX}是一个常量，可能定义在常量类ApiRouterConsts中，表示前台门户首页模块的URL前缀。
 *
 * @RequiredArgsConstructor是一个Lombok注解，用于自动生成构造函数，通过将final修饰的成员变量作为参数来生成一个包含这些成员变量的构造函数。
 */
@Tag(name = "HomeController", description = "前台门户-首页模块")
@RestController
@RequestMapping(ApiRouterConsts.API_FRONT_HOME_URL_PREFIX)
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    /**
     * 首页小说推荐查询接口
     */
    @Operation(summary = "首页小说推荐查询接口")
    @GetMapping("books")
    public RestResp<List<HomeBookRespDto>> listHomeBooks() {
        return homeService.listHomeBooks();
    }

    /**
     * 首页友情链接列表查询接口
     */
    @Operation(summary = "首页友情链接列表查询接口")
    @GetMapping("friend_Link/list")
    public RestResp<List<HomeFriendLinkRespDto>> listHomeFriendLinks() {
        return homeService.listHomeFriendLinks();
    }

}
