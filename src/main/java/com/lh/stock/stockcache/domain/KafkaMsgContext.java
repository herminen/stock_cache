package com.lh.stock.stockcache.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: liuhai
 * @Date: 2020/6/6 16:02
 */
@Getter
@Setter
@ToString
public class KafkaMsgContext<T> {

    private String requestId;

    private String msgType;

    private T cacheData;

    public KafkaMsgContext(String requestId, String msgType) {
        this.requestId = requestId;
        this.msgType = msgType;
    }

    public boolean matchMsg(String msgTag){
        if(StringUtils.isBlank(msgTag) || StringUtils.isBlank(msgType)){
            return false;
        }
        return msgType.equals(msgTag);
    }

}
