package com.heller.nutzbook.bean;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.json.JsonField;

@Getter
@Setter
@Table("t_user_profile")
public class UserProfile extends BasePojo {

    /** 关联用户的id */
    @Id(auto = false)
    @Column("user_id")
    private int userId;

    /** 用户昵称 */
    @Column
    private String nickname;

    @Column
    private String email;

    @Column("email_checked")
    private boolean emailChecked;

    @Column
    @JsonField(ignore = true)
    private byte[] avatar;

    @Column
    private String gender;

    /** 简介 */
    @Column
    private String description;

    @Column
    private String location;

}
