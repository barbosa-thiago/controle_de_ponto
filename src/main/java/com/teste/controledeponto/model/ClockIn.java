package com.teste.controledeponto.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "pnt_clockin")
public class ClockIn extends BaseEntity {

    @Column(name = "clock_in")
    LocalDateTime dateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    User user;



}
