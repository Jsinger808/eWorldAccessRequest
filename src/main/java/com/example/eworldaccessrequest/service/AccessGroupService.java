package com.example.eworldaccessrequest.service;

import com.example.eworldaccessrequest.entity.AccessGroup;
import com.example.eworldaccessrequest.exception.EmptyStringException;
import com.example.eworldaccessrequest.exception.InvalidAccessGroupTypeException;
import com.example.eworldaccessrequest.repository.AccessGroupRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class AccessGroupService {

    @Autowired
    private AccessGroupRepository accessGroupRepository;

    // Save operation
    public AccessGroup saveAccessGroup(AccessGroup accessGroup) throws EmptyStringException {
        if ("".equalsIgnoreCase(accessGroup.getName()) || "".equalsIgnoreCase(accessGroup.getType())) {
            throw new EmptyStringException();
        }
        if (!Arrays.asList("AD", "SECURELINK", "OUTLOOK_EMAIL", "DHS_FORM").contains(accessGroup.getType())) {
            throw new InvalidAccessGroupTypeException();
        }
        return accessGroupRepository.save(accessGroup);
    }

    // Read operation
    public List<AccessGroup> fetchAccessGroupList() {
        return (List<AccessGroup>) accessGroupRepository.findAll();
    }

    // Update operation
    public AccessGroup
    updateAccessGroup(AccessGroup accessGroup, Long ID) {
        AccessGroup depDB = accessGroupRepository.findById(ID).get();

        if (Objects.nonNull(depDB.getName()) && !"".equalsIgnoreCase(depDB.getName())) {
            depDB.setName(depDB.getName());
        }

        if (Objects.nonNull(depDB.getType()) && !"".equalsIgnoreCase(depDB.getType())) {
            depDB.setType(depDB.getType());
        }

        return accessGroupRepository.save(depDB);
    }

    // Delete operation
    public void deleteAccessGroupById(Long ID) {
        accessGroupRepository.deleteById(ID);
    }

}
