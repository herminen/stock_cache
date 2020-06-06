package com.lh.stock.stockcache.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: liuhai
 * @Date: 2020/6/6 16:02
 */
@Getter
@Setter
public class KafkaMsgContext<T> {

    private String requestId;

    private String msgType;

    private T cacheData;

    public boolean matchMsg(String msgTag){
        if(StringUtils.isBlank(msgTag) || StringUtils.isBlank(msgType)){
            return false;
        }
        return msgTag.equals(msgTag);
    }
}
