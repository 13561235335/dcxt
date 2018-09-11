package cn.wzgzs.springboot.common;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestResult implements Serializable {
    private static final long serialVersionUID = -1809195782514717593L;
    public static final RestResult OK = new RestResult(1, "操作成功！");
    public static final RestResult FAIL = new RestResult(0, "操作失败！");
    public static final RestResult FAIL_TIME_OUT = new RestResult(2, "用户登录超时！");
    private int code;
    private String msg;
    private Object data;

    public RestResult() {
        this(1, "操作成功！", (Object)null);
    }

    public RestResult(Object data) {
        this(1, "操作成功！", data);
    }

    public RestResult(int code, String msg) {
        this(code, msg, (Object)null);
    }

    public RestResult(int code, Object data) {
        this(code, "操作成功！", data);
    }

    public RestResult(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    
    /**
     * 自动判断成功,失败
     * @param row 大于0返回"操作成功"/其他返回"操作失败"
     * @author purplebrick
     * @version 2017年12月2日
     */
    public static RestResult auto(Integer row) {
    	if(row > 0) {
    		return OK;
    	}else {
    		return FAIL;
    	}
    }
    
    /**
     * 自动判断成功,失败
     * @param row 大于0返回"操作成功"和数据/其他返回"操作失败"
     * @param data 返回数据
     * @author purplebrick
     * @version 2017年12月2日
     */
    public static RestResult auto(Integer row, Object data) {
    	if(row > 0) {
    		return new RestResult(data);
    	}else {
    		return FAIL;
    	}
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
