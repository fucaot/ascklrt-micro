package com.ascklrt.common.util;

import cn.hutool.core.collection.CollUtil;
import com.ascklrt.common.type.Tuple;
import com.ascklrt.common.type.Tuple4;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author goumang
 * @description
 * @date 2022/10/14 10:13
 */
public class CommonListUtil {

    /**
     * 无顺序比较List方法，必须指定唯一标识（根据唯一标识不可存在重复元素）。
     * 使用此方法请重写需比对对象的equals方法。
     * T为比较List基准对象，R为识别唯一标识（通过R来确定数据是修改还是新增）。
     *
     * @param comp
     * @param base
     * @param fn
     * @return Tuple4<Boolean, List<T>, List<T> , List<T>> v1：比对结果，v2：新增（取新），v3：删除（取老），v4：修改（取新）
     * @author goumang
     * @date 2022/10/14 10:21
     */
    public static <T, R> Tuple4<Boolean, List<T>, List<T> , List<T>> detailEq(List<T> base, List<T> comp, Function<? super T, ? extends R> fn) {
        Boolean r = Boolean.TRUE;
        List<T> adds = new ArrayList<>();
        List<T> dels = new ArrayList<>();
        List<T> mods = new ArrayList<>();

        if (base == null) return Tuple.of(Boolean.FALSE, comp, dels, mods);
        if (comp == null) return Tuple.of(Boolean.FALSE, adds, comp, mods);

        Map<? extends R, T> baseMap = base.stream().collect(Collectors.toMap(fn, Function.identity()));
        for (T v : comp) {
            T t = baseMap.get(fn.apply(v));
            if (t == null) {
                adds.add(v);
                r = Boolean.FALSE;
            } else {
                if (!t.equals(v)) {
                    mods.add(v);
                    r = Boolean.FALSE;
                }
            }
        }

        Map<? extends R, T> compMap = comp.stream().collect(Collectors.toMap(fn, Function.identity()));
        for (T v : base) {
            T t = compMap.get(fn.apply(v));
            if (t == null) {
                dels.add(v);
                r = Boolean.FALSE;
            }
        }
        return Tuple.of(r, adds, dels, mods);
    }
}
