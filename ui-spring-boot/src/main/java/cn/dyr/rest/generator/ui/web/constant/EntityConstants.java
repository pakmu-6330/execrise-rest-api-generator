package cn.dyr.rest.generator.ui.web.constant;

/**
 * 程序的常量表
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class EntityConstants {

    /**
     * User 实体相关的常量
     */
    public static final class UserEntityConstant {

        /**
         * 表示这个用户已经通过邮箱验证
         */
        public static final int USER_STATUS_VERIFIED = 1;

        /**
         * 表示这个用户是等待验证的用户
         */
        public static final int USER_STATUS_TO_BE_VERIFIED = 2;
    }

    /**
     * 表示验证码的类型
     */
    public static final class UserVerifyTokenConstant {

        /**
         * 表示这个验证码是注册验证码
         */
        public static final int VERIFY_CODE_TYPE_REGISTER = 1;
    }

    /**
     * Job 实体相关的常量
     */
    public static final class JobEntityConstant {
        /**
         * 表示系统已经接受这个任务
         */
        public static final int JOB_STATUS_ACCEPT = 1;

        /**
         * 表示系统正在执行这个任务
         */
        public static final int JOB_STATUS_EXECUTING = 2;

        /**
         * 表示系统已经完成这个任务
         */
        public static final int JOB_STATUS_FINISH = 3;

        /**
         * 表示系统执行这个任务失败
         */
        public static final int JOB_STATUS_FAILED = 4;
    }

    /**
     * Attribute 实体相关的常量
     */
    public static final class AttributeEntityConstant {
        public static final int TYPE_BYTE = 1;
        public static final int TYPE_SHORT = 2;
        public static final int TYPE_INT = 3;
        public static final int TYPE_LONG = 4;
        public static final int TYPE_FLOAT = 5;
        public static final int TYPE_DOUBLE = 6;
        public static final int TYPE_FIXED_STRING = 7;
        public static final int TYPE_VAR_STRING = 8;
        public static final int TYPE_BOOLEAN = 9;
        public static final int TYPE_DATETIME = 10;
    }

    /**
     * DBInfoEntity 实体相关的常量
     */
    public static final class DatabaseInfoEntityConstant {
        public static final int TYPE_MIN = 1;
        public static final int TYPE_MAX = 1;

        public static final int TYPE_MYSQL = 1;
    }

    /**
     * Project 实体相关的常量
     */
    public static final class ProjectEntityConstant {
        /**
         * 表示这个工程仅仅是已经创建
         */
        public static final int TYPE_SAVED = 1;
        /**
         * 表示这个工程已经提交生成
         */
        public static final int TYPE_SUBMITTED = 2;
    }

    /**
     * RelationshipEntity 实体相关的常量
     */
    public static final class RelationshipConstant {
        /**
         * 表示关联关系是从 A 端到 B 端单向关联
         */
        public static final int DIRECTION_A_TO_B = 1;

        /**
         * 表示关联关系是从 B 端到 A 端的单向关联
         */
        public static final int DIRECTION_B_TO_A = 2;

        /**
         * 表示这个关联关系是双向的关联关系
         */
        public static final int DIRECTION_BOTH = 3;

        /**
         * 表示一对一的关联关系
         */
        public static final int TYPE_ONE_TO_ONE = 1;

        /**
         * 表示一对多的关联关系
         */
        public static final int TYPE_ONE_TO_MANY = 2;

        /**
         * 表示多对一的关联关系
         */
        public static final int TYPE_MANY_TO_ONE = 3;

        /**
         * 表示多对多的关联关系
         */
        public static final int TYPE_MANY_TO_MANY = 4;
    }
}
