package cn.dyr.rest.generator.ui.swing.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 用于统一产生对象的唯一标识符
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class IdGenerator {

    private static AtomicLong entityLong;
    private static AtomicLong attributeLong;
    private static AtomicLong relationshipLong;

    static {
        entityLong = new AtomicLong(1);
        attributeLong = new AtomicLong(1);
        relationshipLong = new AtomicLong(1);
    }

    /**
     * 产生一个用于属性的唯一标识符
     *
     * @return 用于标识属性的唯一标识符
     */
    public static long attribute() {
        return attributeLong.getAndIncrement();
    }

    /**
     * 产生一个用于实体的唯一标识符
     *
     * @return 用于标识实体的唯一标识符
     */
    public static long entity() {
        return entityLong.getAndIncrement();
    }

    /**
     * 产生一个用于关联关系的唯一标识符
     *
     * @return 用于标识实体的唯一标识符
     */
    public static long relationship() {
        return relationshipLong.getAndIncrement();
    }

}
