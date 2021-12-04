package top.wilsonlv.jaguar.cloud.upms.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.wilsonlv.jaguar.cloud.upms.controller.dto.DeptCreateDTO;
import top.wilsonlv.jaguar.cloud.upms.controller.dto.DeptModifyDTO;
import top.wilsonlv.jaguar.cloud.upms.entity.Dept;
import top.wilsonlv.jaguar.cloud.upms.entity.User;
import top.wilsonlv.jaguar.cloud.upms.mapper.DeptMapper;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.DeptVO;
import top.wilsonlv.jaguar.basecrud.BaseModel;
import top.wilsonlv.jaguar.rediscache.AbstractRedisCacheService;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 部门 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2021-11-15
 */
@Service
@RequiredArgsConstructor
public class DeptService extends AbstractRedisCacheService<Dept, DeptMapper> {

    private final UserService userService;

    public DeptVO getDetail(Long id) {
        Dept dept = this.getCache(id);
        return dept.toVo(DeptVO.class);
    }

    @Transactional
    public List<DeptVO> tree(long parentId) {
        List<Dept> depts = this.list(Wrappers.lambdaQuery(Dept.class)
                .eq(Dept::getParentId, parentId));

        List<DeptVO> deptVos = new ArrayList<>(depts.size());
        for (Dept dept : depts) {
            DeptVO deptVO = dept.toVo(DeptVO.class);
            deptVO.setChildren(this.tree(dept.getId()));
            deptVos.add(deptVO);
        }
        return deptVos;
    }

    @Transactional
    public void create(DeptCreateDTO createDTO) {
        Dept dept = createDTO.toEntity(Dept.class);
        this.insert(dept);
    }

    @Transactional
    public void modify(DeptModifyDTO modifyDTO) {
        Dept dept = modifyDTO.toEntity(Dept.class);
        this.updateById(dept);
    }

    @Transactional
    public void checkAndDelete(Long id) {
        List<User> users = userService.list(Wrappers.lambdaQuery(User.class)
                .eq(User::getUserDeptId, id)
                .select(BaseModel::getId));
        if (users.size() > 0) {
            for (User user : users) {
                user.setUserDeptId(0L);
            }
            userService.batchUpdateById(users);
        }

        this.delete(id);

        List<Dept> children = this.list(Wrappers.lambdaQuery(Dept.class)
                .eq(Dept::getParentId, id)
                .select(BaseModel::getId));
        for (Dept child : children) {
            this.checkAndDelete(child.getId());
        }
    }

}
