package com.heller.nutzbook.bean;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

@Getter
@Setter
@Table("t_user")
public class User extends BasePojo {

    @Id
    private int id;

    @Name
    private String name;

    @Column("password") // 有一个属性加了@Column注解，必须所有的字段都加（加了@Id、@Name的可以不加）
    private String password;

    @Column("salt")
    private String salt;

}
