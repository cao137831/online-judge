package com.cao.oj.common;

import java.io.Serializable;

import lombok.Data;

/**
 * 删除请求
 * @author cao13
 */
@Data
public class DeleteRequest implements Serializable {

    /**
     * 删除 Id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}