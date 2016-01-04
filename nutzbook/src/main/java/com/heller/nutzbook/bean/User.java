package com.heller.nutzbook.bean;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.*;

import java.util.List;

@Getter
@Setter
@Table("t_user")
public class User extends BasePojo {

    @Id
    private int id;

    @Name
    private String name;

    @Column // 有一个属性加了@Column注解，必须所有的字段都加（加了@Id、@Name的可以不加）
    @ColDefine(width=128)
    private String password;

    @Column
    private String salt;

    @Column
    private boolean locked;

    @ManyMany(relation="t_user_role",
            target=Role.class,
            from="u_id",
            to="role_id")
    protected List<Role> roles;

    @ManyMany(relation="t_user_permission",
            target=Permission.class,
            from="u_id",
            to="permission_id")
    protected List<Permission> permissions;

    /** 配置一一映射, User类的id，UserProfile类的userId对应 */
    @One(target = UserProfile.class, field = "id", key = "userId")
    private UserProfile profile;

}
