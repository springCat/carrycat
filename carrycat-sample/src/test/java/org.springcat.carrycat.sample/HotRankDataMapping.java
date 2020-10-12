package org.springcat.carrycat.sample;

import cn.hutool.core.date.DateUtil;
import cn.hutool.db.Entity;
import org.springcat.carrycat.impl.transformer.FtpRdbmsDataMapping;

import java.io.File;
import java.util.Map;

/**
 * @Description DataMapping
 * @Author springCat
 * @Date 2020/10/12 12:44
 */
public class HotRankDataMapping extends FtpRdbmsDataMapping {

    @Override
    public void mapping(String[] rawData, Entity entity) {
        entity.set("CIRCLE_ID", rawData[0]);
        entity.set("STAT_TIME", DateUtil.parse(rawData[1]));
        entity.set("RANK_NO", rawData[2]);
    }

}
