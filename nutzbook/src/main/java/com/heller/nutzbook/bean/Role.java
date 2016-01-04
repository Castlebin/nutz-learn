package com.heller.nutzbook.bean;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.*;

import java.util.List;

@Getter
@Setter
@Table("t_role")
public class Role extends BasePojo {

    @Id
    private long id;

    @Name
    private String name;

    @Column
    private String alias;

    @Column
    @ColDefine(width = 500)
    private String description;

    /** 配置Role和Permission之前的多对多映射 */
    @ManyMany(relation = "t_role_permission",
            target = Permission.class,
            from = "role_id",
            to = "permission_id")
    private List<Permission> permissions;

}
