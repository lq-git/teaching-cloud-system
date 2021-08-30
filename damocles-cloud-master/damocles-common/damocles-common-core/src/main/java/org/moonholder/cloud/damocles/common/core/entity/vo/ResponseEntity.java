package org.moonholder.cloud.damocles.common.core.entity.vo;

import java.util.HashMap;
import java.util.Objects;

/**
 * 操作消息提醒
 *
 * @author ruoyi
 */
public class ResponseEntity extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    public static final String CODE_TAG = "code";

    /**
     * 返回内容
     */
    public static final String MSG_TAG = "msg";

    /**
     * 数据对象
     */
    public static final String DATA_TAG = "data";

    /**
     * 状态类型
     */
    public enum Type {
        /**
         * 成功
         */
        SUCCESS(0),
        /**
         * 警告
         */
        WARN(301),
        /**
         * 未授权
         */
        UNAUTHORIZED(401),
        /**
         * 权限不足
         */
        FORBIDDEN(403),
        /**
         * 错误
         */
        ERROR(500);
        private final int value;

        Type(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }
    }

    /**
     * 初始化一个新创建的 ResponseEntity 对象，使其表示一个空消息。
     */
    public ResponseEntity() {
    }

    /**
     * 初始化一个新创建的 ResponseEntity 对象
     *
     * @param type 状态类型
     * @param msg  返回内容
     */
    public ResponseEntity(Type type, String msg) {
        super.put(CODE_TAG, type.value);
        super.put(MSG_TAG, msg);
    }

    /**
     * 初始化一个新创建的 ResponseEntity 对象
     *
     * @param type 状态类型
     * @param msg  返回内容
     * @param data 数据对象
     */
    public ResponseEntity(Type type, String msg, Object data) {
        super.put(CODE_TAG, type.value);
        super.put(MSG_TAG, msg);
        if (Objects.nonNull(data)) {
            super.put(DATA_TAG, data);
        }
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static ResponseEntity success() {
        return ResponseEntity.success("操作成功");
    }

    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static ResponseEntity success(Object data) {
        return ResponseEntity.success("操作成功", data);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static ResponseEntity success(String msg) {
        return ResponseEntity.success(msg, null);
    }

    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static ResponseEntity success(String msg, Object data) {
        return new ResponseEntity(Type.SUCCESS, msg, data);
    }

    /**
     * 返回警告消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static ResponseEntity warn(String msg) {
        return ResponseEntity.warn(msg, null);
    }

    /**
     * 返回警告消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static ResponseEntity warn(String msg, Object data) {
        return new ResponseEntity(Type.WARN, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @return
     */
    public static ResponseEntity error() {
        return ResponseEntity.error("操作失败");
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static ResponseEntity error(String msg) {
        return ResponseEntity.error(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    private static ResponseEntity error(String msg, Object data) {
        return new ResponseEntity(Type.ERROR, msg, data);
    }

    public static ResponseEntity unauthorized() {
        return ResponseEntity.unauthorized("身份认证已过期");
    }

    public static ResponseEntity unauthorized(String msg) {
        return ResponseEntity.unauthorized(msg, null);
    }

    private static ResponseEntity unauthorized(String msg, Object data) {
        return new ResponseEntity(Type.UNAUTHORIZED, msg, data);
    }

    public static ResponseEntity forbidden() {
        return ResponseEntity.forbidden("权限不足");
    }

    public static ResponseEntity forbidden(String msg) {
        return ResponseEntity.forbidden(msg, null);
    }

    private static ResponseEntity forbidden(String msg, Object data) {
        return new ResponseEntity(Type.FORBIDDEN, msg, data);
    }

}
