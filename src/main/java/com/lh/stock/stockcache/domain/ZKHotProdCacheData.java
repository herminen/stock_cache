package com.lh.stock.stockcache.domain;

import com.google.common.collect.Sets;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;
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
@AllArgsConstructor
@NoArgsConstructor
public class ZKHotProdCacheData implements Serializable {
    private static final long serialVersionUID = 659703363850097606L;

    private String cacheNode;

    private Boolean hasCached;

    private int taskId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ZKHotProdCacheData that = (ZKHotProdCacheData) o;
        return taskId == that.taskId &&
                Objects.equals(cacheNode, that.cacheNode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cacheNode, taskId);
    }
}
