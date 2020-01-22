package com.djl.dmall.pms.service.impl;

import com.djl.dmall.pms.entity.Comment;
import com.djl.dmall.pms.mapper.CommentMapper;
import com.djl.dmall.pms.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品评价表 服务实现类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-22
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

}
