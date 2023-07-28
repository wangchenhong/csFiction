package io.github.xxyopen.novel.manager.cache;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.xxyopen.novel.core.constant.CacheConsts;
import io.github.xxyopen.novel.core.constant.DatabaseConsts;
import io.github.xxyopen.novel.dao.entity.BookInfo;
import io.github.xxyopen.novel.dao.entity.HomeBook;
import io.github.xxyopen.novel.dao.mapper.BookInfoMapper;
import io.github.xxyopen.novel.dao.mapper.HomeBookMapper;
import io.github.xxyopen.novel.dto.resp.HomeBookRespDto;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * 首页推荐小说 缓存管理类
 *
 * @author chenxi
 * @date 2022/5/12
 */

/**
 * `@Component`注解用于将一个类标识为Spring组件，表示它是一个可被Spring容器管理的Bean。
 * 在Spring应用程序中，使用`@Component`注解来定义自己的Bean，
 * 通过自动扫描机制可以将其加载到Spring容器中，并且可以通过@Autowired或@Resource注解进行依赖注入。
 * 通常情况下，`@Component`注解放置在类的上方，用于标注该类为一个组件。
 * 这意味着该类可以被其他类引用、依赖和使用。`HomeBookCacheManager`类被标记为`@Component`，
 * 因此它是一个Spring组件，可以在其他地方通过自动注入的方式使用该类的实例对象。
 */
@Component
/**
 * @RequiredArgsConstructor是一个Lombok注解，它可以自动为标记了@NonNull或者final的成员变量生成对应的构造方法。
 * 当标记了@RequiredArgsConstructor的类中存在其他带参数的构造方法时，该注解不会生成构造方法。
 * HomeBookCacheManager类标记了@RequiredArgsConstructor注解，意味着该类的构造方法会自动生成，
 * 并且会包含final或者标记了@NonNull的成员变量作为参数。
 * HomeBookMapper和BookInfoMapper成员变量会被自动注入到构造方法中，
 * 以便在使用HomeBookCacheManager类时可以直接使用这两个依赖的实例对象。
 */
@RequiredArgsConstructor
public class HomeBookCacheManager {

    private final HomeBookMapper homeBookMapper;

    private final BookInfoMapper bookInfoMapper;

    /**
     * 查询首页小说推荐，并放入缓存中
     *
     */
    //实现缓存
    //指定了缓存管理器为CacheConsts.CAFFEINE_CACHE_MANAGER，缓存名称为CacheConsts.HOME_BOOK_CACHE_NAME
    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
        value = CacheConsts.HOME_BOOK_CACHE_NAME)
    /**
     * 当调用listHomeBooks()方法时，会首先查找缓存中是否存在对应的结果，
     * 如果存在，则直接返回缓存中的结果；如果不存在，则执行方法体内的代码逻辑，并将返回结果放入缓存中。
     */
    public List<HomeBookRespDto> listHomeBooks() {
        // 从首页小说推荐表中查询出需要推荐的小说
        /**
         * 首先，创建了一个QueryWrapper<HomeBook>对象，该对象用于构建数据库查询条件。
         * 然后，调用orderByAsc()方法设置按照升序排序方式对查询结果进行排序，排序的基准列是DatabaseConsts.CommonColumnEnum.SORT.getName()
         * 即根据数据库中名为"SORT"的字段进行排序。接下来，调用homeBookMapper.selectList(queryWrapper)方法执行查询操作，将查询结果存储在homeBooks列表中。
         *
         * 这段代码的作用是查询数据库中的HomeBook表，并按照指定的字段进行升序排序，获取查询结果并保存在homeBooks列表中。
         */
        QueryWrapper<HomeBook> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc(DatabaseConsts.CommonColumnEnum.SORT.getName());
        List<HomeBook> homeBooks = homeBookMapper.selectList(queryWrapper);

        // 获取推荐小说ID列表
        if (!CollectionUtils.isEmpty(homeBooks)) {
            /**
             * 调用homeBooks.stream()将homeBooks列表转换为一个Stream流。
             * 调用map(HomeBook::getBookId)对流中的每个HomeBook对象调用getBookId()方法，将bookId属性提取出来。
             * 调用.toList()将流转换为一个List<Long>对象，其中包含了所有bookId属性的值。
             * 最终，得到的bookIds列表即为homeBooks列表中所有HomeBook对象的bookId属性值的集合。
             */
            List<Long> bookIds = homeBooks.stream()
                .map(HomeBook::getBookId)
                .toList();

            // 根据小说ID列表查询相关的小说信息列表
            QueryWrapper<BookInfo> bookInfoQueryWrapper = new QueryWrapper<>();
            bookInfoQueryWrapper.in(DatabaseConsts.CommonColumnEnum.ID.getName(), bookIds);
            List<BookInfo> bookInfos = bookInfoMapper.selectList(bookInfoQueryWrapper);

            // 组装 HomeBookRespDto 列表数据并返回
            if (!CollectionUtils.isEmpty(bookInfos)) {
                /**
                 * 调用bookInfos.stream()将bookInfos列表转换为一个Stream流。
                 * 调用collect(Collectors.toMap(BookInfo::getId, Function.identity()))，对流中的每个BookInfo对象进行处理。
                 * toMap方法接受两个参数：第一个参数BookInfo::getId表示使用BookInfo对象的id属性作为Map的键；
                 * 第二个参数Function.identity()表示将BookInfo对象本身作为Map的值。
                 * 最终得到的结果是一个Map<Long, BookInfo>对象bookInfoMap，其中以BookInfo对象的id作为键，对应的BookInfo对象作为值。
                 */
                Map<Long, BookInfo> bookInfoMap = bookInfos.stream()
                    .collect(Collectors.toMap(BookInfo::getId, Function.identity()));

                return homeBooks.stream().map(v -> {
                    BookInfo bookInfo = bookInfoMap.get(v.getBookId());
                    HomeBookRespDto bookRespDto = new HomeBookRespDto();
                    bookRespDto.setType(v.getType());
                    bookRespDto.setBookId(v.getBookId());
                    bookRespDto.setBookName(bookInfo.getBookName());
                    bookRespDto.setPicUrl(bookInfo.getPicUrl());
                    bookRespDto.setAuthorName(bookInfo.getAuthorName());
                    bookRespDto.setBookDesc(bookInfo.getBookDesc());
                    return bookRespDto;
                }).toList();

            }

        }

        return Collections.emptyList();
    }

}
