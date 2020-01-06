package com.leyou.item.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpecificationService {

    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;

    public List<SpecGroup> queryGroupByCid(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        List<SpecGroup> list = specGroupMapper.select(specGroup);
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ExceptionEnum.SPEC_GROUP_NOT_FOUND);
        }
        return list;
    }

    public void saveSpeicGroup(SpecGroup specGroup) {
        specGroup.setId(null);

        int count = specGroupMapper.insert(specGroup);
        if (count != 1) {
            throw new LyException(ExceptionEnum.SPEC_GROUP_SAVE_ERRO);
        }
    }

    public void updateSpeicGroup(SpecGroup specGroup) {
        int count = specGroupMapper.updateByPrimaryKey(specGroup);
        if (count != 1) {
            throw new LyException(ExceptionEnum.SPEC_GROUP_UPDATE_ERRO);
        }
    }

    public void deleteSpeicGroup(Long sid) {
        int count = specGroupMapper.deleteByPrimaryKey(sid);
        if (count != 1) {
            throw new LyException(ExceptionEnum.SPEC_GROUP_DELETE_ERRO);
        }
    }

    //* * * * * * * * 规格组参数 * * * * * * * *

    public List<SpecParam> queryParamList(Long gid,Long cid,Boolean generic,Boolean searching) {

        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setGeneric(generic);
        specParam.setSearching(searching);
        List<SpecParam> list = specParamMapper.select(specParam);
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ExceptionEnum.SPEC_PARAM_NOT_FOUND);
        }
        return list;
    }

    public void saveSpecParam(SpecParam specParam) {
        specParam.setId(null);
        int count = specParamMapper.insert(specParam);
        if (count != 1) {
            throw new LyException(ExceptionEnum.SPEC_PARAM_SAVE_ERRO);
        }
    }

    public void updateSpecParam(SpecParam specParam) {
        int count = specParamMapper.updateByPrimaryKey(specParam);
        if (count != 1) {
            throw new LyException(ExceptionEnum.SPEC_PARAM_UPDATE_ERRO);
        }
    }

    public void deleteSpecParam(Long sid) {
        int count = specParamMapper.deleteByPrimaryKey(sid);
        if (count != 1) {
            throw new LyException(ExceptionEnum.SPEC_PARAM_DELETE_ERRO);
        }
    }

    public List<SpecGroup> queryListByCid(Long cid) {
        //查询规格组
        List<SpecGroup> specGroups = queryGroupByCid(cid);
        //查询当前分类下的参数
        List<SpecParam> specParams = queryParamList(null, cid, null, null);
        //先把规格参数变成map，map的key是规格组ID，map的值是组下的所有参数
        Map<Long, List<SpecParam>> map = new HashMap<>();

        for (SpecParam specParam : specParams) {
            if (!map.containsKey(specParam.getGroupId())) {
                //若不存在这个key，那么就新创建一个
                map.put(specParam.getGroupId(), new ArrayList<>());
            }
            map.get(specParam.getGroupId()).add(specParam);
        }
        //填充specParam到group
        for (SpecGroup specGroup : specGroups) {
            specGroup.setParams(map.get(specGroup.getId()));
        }
        return specGroups;
    }
}
