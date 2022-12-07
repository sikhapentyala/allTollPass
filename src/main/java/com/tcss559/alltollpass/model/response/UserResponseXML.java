package com.tcss559.alltollpass.model.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.tcss559.alltollpass.entity.Role;
import lombok.Builder;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@Builder
@JacksonXmlRootElement
@XmlRootElement
public class UserResponseXML {
    private Long id;
    private String username;
    private String password;
    private Role role;
    private String name;
    private String email;
}
