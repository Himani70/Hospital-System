package com.hospital.model;


import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="rule_id", nullable = false)
    private Integer id;

    @Column(name="part_id", nullable = false)
    private Integer partId;

    @Column(name="sym_id", nullable = false)
    private Integer symptomId;

    @Column(name="svr_id", nullable = false)
    private Integer severityId;

    @Column(name="priority_status")
    private Integer priorityStatus;
}
