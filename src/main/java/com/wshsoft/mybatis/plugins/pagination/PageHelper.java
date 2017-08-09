package com.wshsoft.mybatis.plugins.pagination;

import com.wshsoft.mybatis.exceptions.MybatisExtendsException;

/**
 * <p>
 * 分页辅助类
 * </p>
 *
 * @author Carry xie
 * @since 2017-06-24
 */

public class PageHelper {

    // 分页本地线程变量
    private static final ThreadLocal<Pagination> LOCAL_PAGE = new ThreadLocal<>();

    /**
     * <p>
     * 获取总条数
     * </p>
     *
     * @return
     */
    public static int getTotal() {
        if (isPageable()) {
            return LOCAL_PAGE.get().getTotal();
        } else {
            throw new MybatisExtendsException("The current thread does not start paging. Please call before PageHelper.startPage");
        }
    }

    /**
     * <p>
     * 释放资源并获取总条数
     * </p>
     */
    public static int freeTotal() {
        int total = getTotal();
        remove();// 释放资源
        return total;
    }

    /**
     * <p>
     * 获取分页
     * </p>
     *
     * @return
     */
    public static Pagination getPagination() {
        return LOCAL_PAGE.get();
    }

    /**
     * <p>
     * 设置分页
     * </p>
     *
     * @param page
     */
    public static void setPagination(Pagination page) {
        LOCAL_PAGE.set(page);
    }

    /**
     * <p>
     * 启动分页
     * </p>
     *
     * @param current 当前页
     * @param size    页大小
     */
    public static void startPage(int current, int size) {
        LOCAL_PAGE.set(new Pagination(current, size));
    }

    /**
     * <p>
     * 是否存在分页
     * </p>
     *
     * @return
     */
    public static boolean isPageable() {
        return LOCAL_PAGE.get() != null;
    }

    /**
     * <p>
     * 释放资源
     * </p>
     */
    public static void remove() {
        LOCAL_PAGE.remove();
    }
}
