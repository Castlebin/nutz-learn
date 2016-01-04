package com.heller.nutzbook.bean;

import lombok.Data;
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

    private String password;

    private String salt;

    private Date createTime;

    private Date updateTime;

}
