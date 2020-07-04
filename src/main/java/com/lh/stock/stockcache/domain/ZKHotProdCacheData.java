package com.lh.stock.stockcache.domain;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: liuhai
 * Date: 2020/7/4
 * Time: 21:16
 * Description: No Description
 */
@Getter
@Setter
@ToString
public class ZKHotProdCacheData implements Serializable {
    private static final long serialVersionUID = 659703363850097606L;

    private Set<String> cacheNodes = Sets.newHashSetWithExpectedSize(10);

    private Boolean hasCached;

    private int taskId;

    public void addCacheNode(String nodeName){
        cacheNodes.add(nodeName);
    }

}
