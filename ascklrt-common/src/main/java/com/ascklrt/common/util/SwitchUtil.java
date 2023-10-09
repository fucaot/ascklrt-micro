package com.ascklrt.common.util;

import cn.hutool.core.bean.BeanUtil;
import org.springframework.beans.BeanUtils;

/**
 * @author goumang
 * @description
 * @date 2022/4/10 12:01 上午
 */
public class SwitchUtil {

    // 切换bean
    public static <T> T switchBean(Object object, Class<T> clazz) {
        if (BeanUtil.isEmpty(object)) {
            return null;
        }
        T newObj = null;
        try {
            newObj = clazz.newInstance();
            BeanUtils.copyProperties(object, newObj);
        } catch (Exception e) {

        }
        return newObj;
    }
}
