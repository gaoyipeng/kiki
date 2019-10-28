package com.sxdx.kiki.common.entity;

import com.sxdx.kiki.common.entity.system.Dept;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author MrBird
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DeptTree extends Tree<Dept>{

    private Integer orderNum;
}
