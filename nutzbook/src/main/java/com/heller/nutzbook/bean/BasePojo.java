package com.heller.nutzbook.bean;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;

import java.util.Date;

@Getter
@Setter
public abstract class BasePojo {

    @Column("create_time")// 类中有一个属性加了@Column注解，必须所有的字段都加（加了@Id、@Name的可以不加）
    private Date createTime;

    @Column("update_time")
    private Date updateTime;

    @Override
    public String toString() {
        return String.format("/*%s*/%s", super.toString(), Json.toJson(this, JsonFormat.compact()));
    }

}
