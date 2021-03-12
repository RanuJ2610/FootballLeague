package com.fl.service.dto;

import lombok.Data;

@Data
public class TeamDto extends LeagueDto{

    private String team_id;
    private String team_name;
    private String overall_league_position;
}
