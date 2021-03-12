package com.fl.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class LeagueDto extends CountryDto{

    private String league_id;
    private String league_name;
}
