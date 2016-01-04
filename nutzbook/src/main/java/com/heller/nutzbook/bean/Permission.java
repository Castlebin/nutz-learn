package com.heller.nutzbook.bean;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.*;

@Getter
@Setter
@Table("t_permission")
public class Permission extends BasePojo {

    @Id
    private long id;

    @Name
    private String name;

    @Column
    private String alias;

    @Column
    @ColDefine(width = 500)
    private String description;

}
