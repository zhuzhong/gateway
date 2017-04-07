package com.zz.gateway.exception;

public enum OpenApiServiceErrorEnum {
    // TODO:common error errorcode from ald00000 to ald09999
    SYSTEM_SUCCESS("ald00000", "success"), SYSTEM_BUSY("ald00001", "server is busy"), SYSTEM_QUEUE_DEEPTH("ald00002",
            "the queue reached max deepth"), VALIDATE_PARAM_ERROR("ald00100", "输入参数有误！"), REMOTE_INVOKE_ERROR(
            "ald00101", "远程服务错误！"), PARA_NORULE_ERROR("ald00102", "请求参数格式不符合规则"), VALIDATE_ERROR("ald00103", "校验有误"), DATA_OPER_ERROR(
            "ald00104", "数据操作异常"), APPLICATION_ERROR("ald00200", "业务逻辑异常"), APPLICATION_OPER_ERROR("ald00201", "系统业务异常"), DATA_EMPTY_ERROR(
            "ald00300", "查询结果为空"),
    // TODO:gateway error errorcode from ald20000 to ald29999

    // TODO:front error errorcode from ald30000 to ald39999

    // TODO:console error errorcode from ald40000 to ald49999

    // TODO:batch error errorcode from ald50000 to ald59999

    ;
    // 成员变量
    private String errCode;
    private String errMsg;

    // 构造方法
    private OpenApiServiceErrorEnum(String errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    // 普通方法
    public static String getErrMsg(String errCode) {
        for (OpenApiServiceErrorEnum c : OpenApiServiceErrorEnum.values()) {
            if (c.getErrCode().equals(errCode)) {
                return c.getErrMsg();
            }
        }
        return null;
    }

    public static OpenApiServiceErrorEnum getErr(String errCode) {
        for (OpenApiServiceErrorEnum c : OpenApiServiceErrorEnum.values()) {
            if (c.getErrCode().equals(errCode)) {
                return c;
            }
        }
        return null;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
