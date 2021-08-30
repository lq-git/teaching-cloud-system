package org.moonholder.cloud.damocles.authority.service.impl;


import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.moonholder.cloud.damocles.authority.mapper.AuthorityMapper;
import org.moonholder.cloud.damocles.authority.service.IAuthorityService;
import org.moonholder.cloud.damocles.common.core.entity.Authority;
import org.moonholder.cloud.damocles.common.core.entity.User;
import org.moonholder.cloud.damocles.authority.util.AuthorityTree;
import org.moonholder.cloud.damocles.common.core.util.Calculator;
import org.moonholder.cloud.damocles.authority.feign.SecurityFeign;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 后台用户权限表 服务实现类
 * </p>
 *
 * @author MoonHolder
 * @since 2020-12-19
 */
@Service
@CacheConfig(cacheNames = "authority")
public class AuthorityServiceImpl extends ServiceImpl<AuthorityMapper, Authority> implements IAuthorityService {

    @Resource
    private AuthorityTree authorityTree;
    @Resource
    private SecurityFeign securityFeign;

    @Override
    @Cacheable(key = "#p0.getHeader('Authorization')")
    public List<Authority> getMenuData(HttpServletRequest request) {
        User user = securityFeign.findUserByRequest();
        List<Authority> authorities = baseMapper.findAuthByUsername(user.getUsername());
        return authorityTree.generateAuthorityTree(authorities, 2);
    }

    @Override
    @Cacheable(key = "#username")
    public List<Authority> findAuthorityByUsername(String username) {
        return baseMapper.findAuthByUsername(username);
    }

    @Override
    public Page<Authority> findAuthorityByCondition(Authority authority, Page<Authority> page) {
        Page<Authority> authorityPage = new LambdaQueryChainWrapper<>(baseMapper).eq(Objects.nonNull(authority.getId()), Authority::getId, authority.getId())
                .eq(Objects.nonNull(authority.getPid()), Authority::getPid, authority.getPid())
                .like(StringUtils.hasLength(authority.getName()), Authority::getName, authority.getName())
                .eq(Objects.nonNull(authority.getType()), Authority::getType, authority.getType())
                .eq(Objects.nonNull(authority.getStatus()), Authority::getStatus, authority.getStatus()).page(page);
        return authorityPage;
    }

    @Override
    public List<Authority> findAuthorityByRoleId(Integer roleId) {
        return baseMapper.selectAuthorityByRoleId(roleId);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"authority"}, allEntries = true)
    public void assignAuthority(Integer roleId, Integer[] authIds, boolean authType) {
        List<Integer> ownedAuthIds = findAuthorityByRoleId(roleId).stream().filter(authority -> authType ? authority.getType() == 2 : (authority.getType() == 0 || authority.getType() == 1)).map(Authority::getId).collect(Collectors.toList());
        Integer[] ownedAuthArray = new Integer[ownedAuthIds.size()];
        ownedAuthArray = ownedAuthIds.toArray(ownedAuthArray);
        // 计算撤销的权限和新增权限
        List<Integer> addition = Calculator.calcDifference(ownedAuthArray, authIds);
        if (!addition.isEmpty()) baseMapper.setAuthTheRole(roleId, addition);
        List<Integer> delete = Calculator.calcDifference(authIds, ownedAuthArray);
        if (!delete.isEmpty()) baseMapper.deleteAuthTheRole(roleId, delete);
    }

    @Override
    @Cacheable(key = "'authTree'")
    public List<Authority> getAuthTree() {
        return authorityTree.generateAuthorityTree(list());
    }
}
