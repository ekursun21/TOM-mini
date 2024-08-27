package com.TOM.tom_mini.crm.mapper;

import com.TOM.tom_mini.crm.entity.Customer;
import com.TOM.tom_mini.crm.response.CustomerInfoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "surname", target = "surname")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "birthday", target = "birthday")
    @Mapping(source = "created_at", target = "created_at")
    @Mapping(source = "modified_at", target = "modified_at")
    CustomerInfoResponse customerToCustomerInfoResponse(Customer customer);
}
