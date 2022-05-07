package com.example.eworldaccessrequest.service;

import com.example.eworldaccessrequest.dto.AccessGroupDTO;
import com.example.eworldaccessrequest.entity.AccessGroup;
import com.example.eworldaccessrequest.exception.EmptyStringException;
import com.example.eworldaccessrequest.exception.InvalidAccessGroupTypeException;
import com.example.eworldaccessrequest.repository.AccessGroupRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class AccessGroupService {

    @Autowired
    private AccessGroupRepository accessGroupRepository;

//    private final AccessGroupRepository accessGroupRepository;
//
//    public AccessGroupService(AccessGroupRepository accessGroupRepository) {
//        this.accessGroupRepository = accessGroupRepository;
//    }

    // DTO Converter
    public static AccessGroupDTO convertToDto(AccessGroup accessGroup) {

        AccessGroupDTO accessGroupDTO = new AccessGroupDTO();
        accessGroupDTO.setID(accessGroup.getID());
        accessGroupDTO.setName(accessGroup.getName());
        accessGroupDTO.setType(accessGroup.getType());
        return accessGroupDTO;
    }

    // Save operation
    public AccessGroupDTO saveAccessGroup(AccessGroup accessGroup) throws EmptyStringException, InvalidAccessGroupTypeException {
        if ("".equalsIgnoreCase(accessGroup.getName()) || "".equalsIgnoreCase(accessGroup.getType())) {
            throw new EmptyStringException();
        }
        if (!Arrays.asList("AD", "SECURELINK", "OUTLOOK_EMAIL", "DHS_FORM").contains(accessGroup.getType())) {
            throw new InvalidAccessGroupTypeException();
        }
        return convertToDto(accessGroupRepository.save(accessGroup));
    }

    // Read operation
    public List<AccessGroupDTO> fetchAccessGroupList() {
        List<AccessGroup> accessGroups = accessGroupRepository.findAll();
        List<AccessGroupDTO> accessGroupDTOs = new ArrayList();

        for (AccessGroup accessGroup : accessGroups) {
            accessGroupDTOs.add(convertToDto(accessGroup));
        }
        return accessGroupDTOs;
    }

    // Update operation
    public AccessGroupDTO updateAccessGroup(AccessGroup accessGroup, Long ID) {
        AccessGroup depDB = accessGroupRepository.findById(ID).get();

        if (Objects.nonNull(accessGroup.getName()) && !"".equalsIgnoreCase(accessGroup.getName())) {
            depDB.setName(accessGroup.getName());
        }

        if (Objects.nonNull(accessGroup.getType()) && !"".equalsIgnoreCase(accessGroup.getType())) {
            depDB.setType(accessGroup.getType());
        }
        return convertToDto(accessGroupRepository.save(depDB));
    }

    // Delete operation
    public void deleteAccessGroupById(Long ID) {
        accessGroupRepository.deleteById(ID);
    }

}
