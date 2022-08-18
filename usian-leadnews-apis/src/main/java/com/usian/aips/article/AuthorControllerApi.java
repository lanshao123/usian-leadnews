package com.usian.aips.article;

import com.usian.model.article.pojos.ApAuthor;
import com.usian.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PathVariable;

@Api(value = "作者管理",tags = "author",description = "作者管理API")
public interface AuthorControllerApi {
    /**
     *根据用户id查询作者信息
     * @param id
     * @return
     */
     ApAuthor findByUserId(Integer id);

    /**
     * 保存作者
     * @param apAuthor
     * @return
     */
     ResponseResult save(ApAuthor apAuthor);
    /**
     * 根据名称查询作者
     * @param name
     * @return
     */
    public ApAuthor findByName(@PathVariable("name") String name);
}
