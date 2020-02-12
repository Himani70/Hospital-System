package com.hospital.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Table(name = "scale")
public class SeverityScale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="scale_id", nullable = false)
    private Integer id;

    @Column(name="sym_id", nullable = false)
    private Integer symptomId;

    @Column(name="svr_id", nullable = false)
    private Integer severityId;

}
