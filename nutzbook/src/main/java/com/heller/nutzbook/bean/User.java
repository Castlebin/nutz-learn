package com.heller.nutzbook.bean;

import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

@Data
@Table("t_user")
public class User {

    @Id
    private int id;

    @Name
    private String name;

    @Column("password") // 有一个属性加了@Column注解，必须所有的字段都加（加了@Id、@Name的可以不加）
    private String password;

    @Column("salt")
    private String salt;

    @Column("create_time")
    private Date createTime;

    @Column("update_time")
    private Date updateTime;

}
