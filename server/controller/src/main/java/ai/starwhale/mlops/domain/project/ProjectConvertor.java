/*
 * Copyright 2022.1-2022
 * StarWhale.ai All right reserved. This software is the confidential and proprietary information of
 * StarWhale.ai ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with StarWhale.ai.
 */

package ai.starwhale.mlops.domain.project;

import ai.starwhale.mlops.api.protocol.project.ProjectVO;
import ai.starwhale.mlops.common.Convertor;
import ai.starwhale.mlops.common.IDConvertor;
import ai.starwhale.mlops.domain.user.UserConvertor;
import ai.starwhale.mlops.exception.ConvertException;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProjectConvertor implements Convertor<ProjectEntity, ProjectVO> {

    @Resource
    private IDConvertor idConvertor;

    @Resource
    private UserConvertor userConvertor;

    @Override
    public ProjectVO convert(ProjectEntity entity) throws ConvertException {
        return ProjectVO.builder()
            .id(idConvertor.convert(entity.getId()))
            .name(entity.getProjectName())
            //.ownerName(entity.getOwnerName())
            .owner(userConvertor.convert(entity.getOwner()))
            .createTime(entity.getCreatedTime().toString())
            .build();
    }

    @Override
    public ProjectEntity revert(ProjectVO vo) throws ConvertException {
        return ProjectEntity.builder()
            .id(idConvertor.revert(vo.getId()))
            .projectName(vo.getName())
            .build();
    }
}
