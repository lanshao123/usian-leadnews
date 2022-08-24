package com.usian.model.media.vos;

import com.usian.model.media.pojos.WmNews;
import lombok.Data;

/**
 * @program: usian-leadnews
 * @description: WmNewsVo
 * @author: wangheng
 * @create: 2022-08-23 16:06
 **/
@Data
public class WmNewsVo extends WmNews {
    private String authorName;
}
