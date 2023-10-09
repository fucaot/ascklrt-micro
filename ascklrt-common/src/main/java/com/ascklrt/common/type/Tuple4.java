package com.ascklrt.common.type;

/**
 * @author goumang
 * @description
 * @date 2022/10/14 16:14
 */
public class Tuple4<T1, T2, T3, T4> {

    public final T1 _1;
    public final T2 _2;
    public final T3 _3;
    public final T4 _4;

    public Tuple4(T1 _1, T2 _2, T3 _3, T4 _4) {
        this._1 = _1;
        this._2 = _2;
        this._3 = _3;
        this._4 = _4;
    }
}
