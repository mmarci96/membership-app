package com.codecool.sv_server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "membership_packages")
@Getter
@Setter
@NoArgsConstructor
public class MembershipPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @OneToMany(mappedBy = "membershipPackage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MembershipModule> modules = new ArrayList<>();

    // Helper methods to manage the bidirectional relationship
    public void addModule(MembershipModule module) {
        modules.add(module);
        module.setMembershipPackage(this);
    }

    public void removeModule(MembershipModule module) {
        modules.remove(module);
        module.setMembershipPackage(null);
    }
}
