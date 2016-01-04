package com.heller.nutzbook.bean;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.*;

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

    /** 配置一一映射, User类的id，UserProfile类的userId对应 */
    @One(target = UserProfile.class, field = "id", key = "userId")
    private UserProfile profile;

}
