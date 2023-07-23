package com.halfacode.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.halfacode.entity.Role;
import com.halfacode.repoistory.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
public class RoleDeserializer{
//public class RoleDeserializer extends JsonDeserializer<Set<Role>> {

   // @Autowired
   /* private RoleRepository roleRepository;

    @Override
    public Set<Role> deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);
        Set<Role> roles = new HashSet<>();

        for (JsonNode roleIdNode : node) {
            Long roleId = roleIdNode.asLong();
            Role role = roleRepository.findById(roleId).orElse(null);
            if (role != null) {
                roles.add(role);
            }
        }

        return roles;
    }*/
}
