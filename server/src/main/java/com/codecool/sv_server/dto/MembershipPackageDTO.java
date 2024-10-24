package com.codecool.sv_server.dto;

import java.util.List;

public record MembershipPackageDTO(long id, String packageName,
                                   List<MembershipModuleDTO> modules) {
}
